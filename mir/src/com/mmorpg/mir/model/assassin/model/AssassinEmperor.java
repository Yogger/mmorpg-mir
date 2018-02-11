package com.mmorpg.mir.model.assassin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.h2.util.New;

import com.mmorpg.mir.model.assassin.config.AssassinConfig;
import com.mmorpg.mir.model.assassin.controller.AssassinController;
import com.mmorpg.mir.model.assassin.packet.SM_Assassin_Random_Reward;
import com.mmorpg.mir.model.assassin.packet.SM_Assassination_End;
import com.mmorpg.mir.model.assassin.packet.SM_Assassination_Square;
import com.mmorpg.mir.model.assassin.packet.SM_Assassination_Start;
import com.mmorpg.mir.model.assassin.packet.SM_Assassination_Status;
import com.mmorpg.mir.model.assassin.packet.SM_Before_Stun;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class AssassinEmperor {

	/** 战斗进行中 */
	private volatile boolean started;

	// <hp%, time>
	private NonBlockingHashMap<Integer, Long> hpStatus;

	// 活动NPC
	private Npc activityNpc;

	private Future<?> skillUseFuture;

	private Future<?> actStatusNoticeFuture;

	private WorldMapInstance wordMapInstance;
	
	/** 排名奖励情况 */
	private Map<Long, TechCopyRankVO> rewardStatus;
	
	private HashMap<Integer, AssassinRandomSet> randomSets;
	
	private long startTime;
	
	private volatile long nextSkillTime;

	public AssassinEmperor(WorldMapInstance instance) {
		this.wordMapInstance = instance;
		this.hpStatus = new NonBlockingHashMap<Integer, Long>();
		this.started = false;
		this.rewardStatus = new HashMap<Long, TechCopyRankVO>();
		this.randomSets = new HashMap<Integer, AssassinRandomSet>();
	}

	public void start() {
		if (started == true) {
			return;
		}
		started = true;
		rewardStatus.clear();
		randomSets.clear();
		hpStatus.clear();
		for (Integer hpConfig : AssassinConfig.getInstance().HPLESSTHAN_LIGHT.getValue()) {
			randomSets.put(hpConfig, new AssassinRandomSet());
		}
		startTime = System.currentTimeMillis();
			
		spawnActivityNpc();

		startScheduledTask();
		
		SessionManager.getInstance().sendAllIdentified(new SM_Assassination_Start());
	}

	private void startScheduledTask() {
		setNextSkillTime(System.currentTimeMillis() + 10000L + AssassinConfig.getInstance().BOSS_SKILL_CD.getValue() * 1000L);
		if (skillUseFuture == null || skillUseFuture.isCancelled()) {
			skillUseFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					if (activityNpc == null || !activityNpc.isSpawned()) {
						skillUseFuture.cancel(false);
						return;
					}
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							useSkill();
							setNextSkillTime(System.currentTimeMillis() + AssassinConfig.getInstance().BOSS_SKILL_CD.getValue() * 1000L);
						}
					}, 5000);
					PacketSendUtility.broadcastPacket(activityNpc, new SM_Before_Stun());

				}
			}, 5000, AssassinConfig.getInstance().BOSS_SKILL_CD.getValue() * 1000L);
		}
		if (actStatusNoticeFuture == null || actStatusNoticeFuture.isCancelled()) {
			actStatusNoticeFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					wordMapInstance.sendPackAll(SM_Assassination_Status.valueOf(activityNpc));
				}
			}, 5000, 5000);
		}
	}

	private void useSkill() {
		Integer bossSkill = AssassinConfig.getInstance().BOSS_STUN_SKILL.getValue();
		for (VisibleObject object : activityNpc.getKnownList()) {

			if (object instanceof Player) {
				Skill skill = SkillEngine.getInstance().getSkill(activityNpc, bossSkill, object.getObjectId(), 0, 0,
						(Creature) object, null);
				boolean isInRange = MathUtil.isInRange(activityNpc, object, skill.getRange(), skill.getRange());
				if (isInRange) {
					skill.noEffectorUseSkill();
				}
			}
		}
	}

	private void spawnActivityNpc() {
		if (activityNpc != null) {
			activityNpc.getController().delete();
		}
		activityNpc = (Npc) SpawnManager.getInstance().creatObject(AssassinConfig.getInstance().BOSSID.getValue(), 1);
		AssassinController controller = new AssassinController();
		controller.setOwner(activityNpc);
		activityNpc.setController(controller);
		activityNpc.getObserveController().addObserver(
				new ActionObserver(ObserverType.DIE, ObserverType.ATTACKED, ObserverType.ROUTEOVER) {
					@Override
					public void attacked(Creature creature) {
						beenAttacked(creature);
					}

					@Override
					public void routeOver() {
						end(null);
					}
				});
		SpawnManager.getInstance().reConstructNpcDynamicStats(activityNpc, activityNpc.getObjectResource(), 
				ServerState.getInstance().getAssassinNpcLevel());
		activityNpc.getLifeStats().synchronizeWithMaxStats();
		SpawnManager.getInstance().bringIntoWorld(activityNpc, 1);
	}
	

	private void deadTooEarly() {
		if (AssassinConfig.getInstance().isTooEarlyDead(startTime)) {
			ServerState.getInstance().upgradeAssassinNpcLevel();
		}
	}


	public void end(Player lastAttacker) {
		if (started == false) {
			return;
		}
		started = false;
		ArrayList<TechCopyRankVO> ranks = doReward(lastAttacker);

		for (Long playerId : rewardStatus.keySet()) {
			Player player = PlayerManager.getInstance().getPlayer(playerId);
			TechCopyRankVO lastAttackerVO = (lastAttacker != null ? rewardStatus.get(lastAttacker.getObjectId()): null);
			SM_Assassination_Square sm = SM_Assassination_Square.valueOf(rewardStatus.get(player.getObjectId()), 
					lastAttackerVO, ranks, System.currentTimeMillis());
			PacketSendUtility.sendPacket(player, sm);
		}
		SessionManager.getInstance().sendAllIdentified(new SM_Assassination_End());
		wordMapInstance.sendPackAll(SM_Assassination_Status.valueOf(activityNpc));
		clearAndCancelAll();
	}

	private void clearAndCancelAll() {
		if (skillUseFuture != null) {
			skillUseFuture.cancel(false);
		}
		if (activityNpc != null) {
			activityNpc.getController().delete();
		}
		if (actStatusNoticeFuture != null) {
			actStatusNoticeFuture.cancel(false);
		}
	}

	private void beenAttacked(Creature creature) {
		long currentHp = activityNpc.getLifeStats().getCurrentHp();
		long maxHp = activityNpc.getLifeStats().getMaxHp();
		int hpPercent = (int) ((currentHp * 1.0 / maxHp) * 100.0);
		for (Integer configPercent : AssassinConfig.getInstance().HPLESSTHAN_LIGHT.getValue()) {
			if (hpPercent < configPercent) {
				Long time = hpStatus.putIfAbsent(configPercent, System.currentTimeMillis());
				if (time == null) {
					triggerRandomReward(configPercent);
				}
			}
		}
	}

	private ArrayList<TechCopyRankVO> doReward(Player lastAttacker) {
		ArrayList<TechCopyRankVO> rankVOs = New.arrayList();
		Map<Integer, Player> ranks = activityNpc.getDamageRank();
		Map<Player, Long> damages = activityNpc.getDamages();
		for (Entry<Integer, Player> rank : ranks.entrySet()) {
			// 构建奖励,邮件
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(rank.getKey(),
					AssassinConfig.getInstance().JKCQ_DAMAGE_REWARD_CHOOSERGROUP.getValue());
			List<String> factor = ChooserManager.getInstance().chooseValueByRequire(rank.getKey(),
					AssassinConfig.getInstance().JKCQ_DAMAGE_FACTOR_CHOOSERGROUP.getValue());
			Map<String, Object> params = New.hashMap();
			params.put("LEVEL", rank.getValue().getLevel());
			params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(rank.getValue()));
			params.put("STANDARD_COINS", PlayerManager.getInstance().getStandardCoins(rank.getValue()));
			params.put("DAMAGE_FACTOR", factor.get(0));
			params.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(rank.getValue()));
			Reward reward = RewardManager.getInstance().creatReward(rank.getValue(), rewardIds, params);
			I18nUtils titel18n = I18nUtils.valueOf(AssassinConfig.getInstance().MAIL_TITLE_CHOOSERGROUP.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(AssassinConfig.getInstance().MAIL_CONTENT_CHOOSERGROUP.getValue());
			contextl18n.addParm("rank", I18nPack.valueOf(rank.getKey()));
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, rank.getValue().getObjectId());
			Long damage = damages.get(rank.getValue());
			TechCopyRankVO vo = TechCopyRankVO.valueOf(rank.getKey(), damage, rank.getValue(), reward);
			if (rank.getKey() <= 3) { // config
				rankVOs.add(vo);
			}
			rewardStatus.put(rank.getValue().getObjectId(), vo);
		}
		if (lastAttacker != null) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(lastAttacker,
					AssassinConfig.getInstance().JKCQ_LAST_ATTACK_REWARD_CHOOSERGROUP.getValue());
			Reward reward = RewardManager.getInstance().creatReward(lastAttacker, rewardIds, null);
			I18nUtils titel18n = I18nUtils.valueOf(AssassinConfig.getInstance().LAST_ATTACK_MAIL_TITLE.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(AssassinConfig.getInstance().LAST_ATTACK_MAIL_CONTENT
					.getValue());
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, lastAttacker.getObjectId());
			I18nUtils utils = I18nUtils.valueOf("801010");
			utils.addParm("name", I18nPack.valueOf(lastAttacker.getName()));
			utils.addParm("country", I18nPack.valueOf(lastAttacker.getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);
			I18nUtils chatUtils = I18nUtils.valueOf("307020", utils);
			ChatManager.getInstance().sendSystem(0, chatUtils, null);
		}
		return rankVOs;
	}

	// trigger random reward notice login
	private void triggerRandomReward(final int hpPercent) {
		final AssassinRandomSet set = randomSets.get(hpPercent);
		set.randPoinst(hpPercent, activityNpc.getKnownList().iterator());
		final ArrayList<AssassinRandVO> vos = set.getResult();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			
			@Override
			public void run() {
				// doReward
				if (set.getRandomArrays().isEmpty()) {
					return;
				}
				
				AssassinRandVO winVO = set.getRandomArrays().get(0);
				Long winPlayerId = winVO.getPlayerId();
				Player player = PlayerManager.getInstance().getPlayer(winPlayerId);
				Reward reward = RewardManager.getInstance().createRewardByChooserGroupId(player, 
						AssassinConfig.getInstance().JKCQ_ROLL_REWARD_CHOOSERGROUP.getValue());
				if (SessionManager.getInstance().isOnline(winPlayerId)) {
					RewardManager.getInstance().grantReward(player, reward, 
							ModuleInfo.valueOf(ModuleType.ASSASSINATION, SubModuleType.ASSASSINATION_ROLL));
				} else {
					I18nUtils title = I18nUtils.valueOf(AssassinConfig.getInstance().RANDOM_MAIL_TITLE.getValue());
					I18nUtils content = I18nUtils.valueOf(AssassinConfig.getInstance().RANDOM_MAIL_CONTENT.getValue());
					Mail mail = Mail.valueOf(title, content, null, reward);
					MailManager.getInstance().sendMail(mail, winPlayerId);
				}
				
				for (AssassinRandVO vo : set.getRandomArrays()) {
					PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(vo.getPlayerId()), 
							SM_Assassin_Random_Reward.valueOf(hpPercent, vos, vo.getRandPoints()));
				}
				
				// notice
				I18nUtils utils = I18nUtils.valueOf("801011");
				utils.addParm("name", I18nPack.valueOf(player.getName()));
				utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
				utils.addParm("num", I18nPack.valueOf(winVO.getRandPoints()));
				ChatManager.getInstance().sendSystem(11001, utils, null);
				I18nUtils chatUtils = I18nUtils.valueOf("307021", utils);
				ChatManager.getInstance().sendSystem(0, chatUtils, null);
			}
		}, 10000L);
	}

	public boolean isWarring() {
		return started;
	}

	public Map<Long, TechCopyRankVO> getRewardRanks() {
		return rewardStatus;
	}

	public int throwDicePoints(int hp, long playerId) {
		AssassinRandomSet set = randomSets.get(hp);
		if (set == null) {
			return 0;
		}
		for (AssassinRandVO vo : set.getRandomArrays()) {
			if (vo.getPlayerId() == playerId) {
				return vo.getRandPoints();
			}
		}
		return 0;
	}

	public Npc getActivityNpc() {
		return activityNpc;
	}

	public long getNextSkillTime() {
		return nextSkillTime;
	}

	public void setNextSkillTime(long nextSkillTime) {
		this.nextSkillTime = nextSkillTime;
	}

	public void die(Creature creature) {
		Player lastAttacker = null;
		if (creature instanceof Player) {
			lastAttacker = (Player) creature;
		} else if (creature instanceof Summon) {
			lastAttacker = ((Summon) creature).getMaster();
		}
		deadTooEarly();
		end(lastAttacker);		
	}
}
