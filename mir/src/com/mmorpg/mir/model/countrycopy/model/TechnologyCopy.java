package com.mmorpg.mir.model.countrycopy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.countrycopy.controller.CountryCopyBossController;
import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_End;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_MonsterHunt;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_Monster_Spawn;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_Start;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_Status;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.manager.SkillManager;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class TechnologyCopy {

	private CountryId countryId;

	private volatile boolean warring;

	private WorldMapInstance worldMapInstance;

	private Future<?> npcControllFuture;

	private Future<?> npcSpawnNoticeFuture;

	private Future<?> bossSkillControllFuture;

	private Future<?> noticeFuture;

	/*** 小怪 */
	private List<VisibleObject> visibleObjects = new CopyOnWriteArrayList<VisibleObject>();

	private String nextSpawnKey = "";

	private volatile long nextSpawnTime;

	private volatile Boss boss;

	private long endTime;

	/** 排名奖励情况 */
	private Map<Long, TechCopyRankVO> rewardStatus = new HashMap<Long, TechCopyRankVO>();

	public TechnologyCopy(CountryId id) {
		this.countryId = id;
	}

	synchronized public void start() {
		if (!CountryCopyConfig.getInstance().getTechCopyStartCond()
				.verify(CountryManager.getInstance().getCountryById(countryId))) {
			return;
		}
		if (isWarring()) {
			return;
		}
		warring = true;
		rewardStatus.clear();

		spawnBoss();

		startCopyTask();

		nextSpawnTime = System.currentTimeMillis()
				+ CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND;
		nextSpawnKey = ChooserManager
				.getInstance()
				.chooseValueByRequire(new Object(),
						CountryCopyConfig.getInstance().TECH_MONSTERS_SPAWN_CHOOSER_ID.getValue()).get(0);

		CountryManager.getInstance().getCountryById(countryId).sendPackAll(SM_TechCopy_Start.valueOf());
	}

	synchronized public void end(Creature creature) {
		Player lastAttacker = null;
		if (creature instanceof Player) {
			lastAttacker = (Player) creature;
		} else if (creature instanceof Summon) {
			lastAttacker = ((Summon) creature).getMaster();
		}
		if (!isWarring()) {
			return;
		}
		warring = false;
		endTime = System.currentTimeMillis();
		clearAllTask();
		clearMonsters();
		ArrayList<TechCopyRankVO> ranks = doReward(lastAttacker);

		for (Player player : CountryManager.getInstance().getCountryById(countryId).getCivils().values()) {
			/*
			 * if
			 * (!CountryCopyConfig.getInstance().getTechCopyEnterCond().verify
			 * (player)) { continue; }
			 */

			SM_TechCopy_End sm = SM_TechCopy_End.valueOf(rewardStatus.get(player.getObjectId()),
					creature != null ? rewardStatus.get(lastAttacker.getObjectId()) : null, ranks, endTime);
			PacketSendUtility.sendPacket(player, sm);
		}
		clearBoss();
	}

	private void startCopyTask() {
		clearAllTask();
		if (noticeFuture == null || noticeFuture.isCancelled()) {
			noticeFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					doSendCopyStatus();
				}

			}, 5000, 5000);
		}

		if (bossSkillControllFuture == null || bossSkillControllFuture.isCancelled()) {
			bossSkillControllFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					bossUseSkill();
				}

			}, CountryCopyConfig.getInstance().BOSS_USE_SKILL_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND,
					CountryCopyConfig.getInstance().BOSS_USE_SKILL_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND);
		}

		if (npcControllFuture == null || npcControllFuture.isCancelled()) {
			npcControllFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(
					new Runnable() {

						@Override
						public void run() {
							spawnMonsters();
							nextSpawnTime = System.currentTimeMillis()
									+ CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue()
									* DateUtils.MILLIS_PER_SECOND;
							nextSpawnKey = ChooserManager
									.getInstance()
									.chooseValueByRequire(new Object(),
											CountryCopyConfig.getInstance().TECH_MONSTERS_SPAWN_CHOOSER_ID.getValue())
									.get(0);
						}
					},
					CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND,
					CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND);
		}

		if (npcSpawnNoticeFuture == null || npcSpawnNoticeFuture.isCancelled()) {
			npcSpawnNoticeFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(
					new Runnable() {

						@Override
						public void run() {
							SM_TechCopy_Monster_Spawn sm = SM_TechCopy_Monster_Spawn.valueOf(nextSpawnTime,
									nextSpawnKey);
							Iterator<Player> playerIterator = worldMapInstance.playerIterator();
							while (playerIterator.hasNext()) {
								PacketSendUtility.sendPacket(playerIterator.next(), sm);
							}
						}
					},
					CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND
							- 10 * DateUtils.MILLIS_PER_SECOND,
					CountryCopyConfig.getInstance().MONSTER_REFRESH_INTERVAL.getValue() * DateUtils.MILLIS_PER_SECOND);
		}
	}

	public void clearAllTask() {
		if (noticeFuture != null) {
			noticeFuture.cancel(false);
		}
		if (npcControllFuture != null) {
			npcControllFuture.cancel(false);
		}
		if (bossSkillControllFuture != null) {
			bossSkillControllFuture.cancel(false);
		}
		if (npcSpawnNoticeFuture != null) {
			npcSpawnNoticeFuture.cancel(false);
		}
	}

	private void bossUseSkill() {
		if (boss == null || !boss.isSpawned()) {
			bossSkillControllFuture.cancel(false);
			return;
		}
		Integer bossSkill = CountryCopyConfig.getInstance().BOSS_STUN_SKIILL_ID.getValue();
		for (VisibleObject object : boss.getKnownList()) {

			if (object instanceof Player) {
				Skill skill = SkillEngine.getInstance().getSkill(boss, bossSkill, object.getObjectId(), 0, 0,
						(Creature) object, null);
				boolean isInRange = MathUtil.isInRange(boss, object, skill.getRange(), skill.getRange());
				if (isInRange) {
					skill.noEffectorUseSkill();
				}
			}
		}
	}

	private void spawnBoss() {
		if (boss != null) {
			boss.getController().delete();
		}
		boss = (Boss) SpawnManager.getInstance().creatObject(
				CountryCopyConfig.getInstance().TECH_BOSS_SPAWNKEY.getValue(), countryId.getValue());
		boss.setCountry(MapCountry.valueOf(countryId.getValue()));
		CountryCopyBossController controller = new CountryCopyBossController();
		controller.setOwner(boss);
		boss.setController(controller);
		boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {

			@Override
			public void die(Creature creature) {
				end(creature);
			}

		});
		SpawnManager.getInstance().bringIntoWorld(boss, countryId.getValue());
	}

	private void spawnMonsters() {
		clearMonsters();
		String spawn = null;
		if (nextSpawnKey == null || nextSpawnKey.isEmpty()) {
			spawn = ChooserManager
					.getInstance()
					.chooseValueByRequire(new Object(),
							CountryCopyConfig.getInstance().TECH_MONSTERS_SPAWN_CHOOSER_ID.getValue()).get(0);
		} else {
			spawn = nextSpawnKey;
		}
		final Npc npc = (Npc) SpawnManager.getInstance().creatObject(spawn, countryId.getValue());
		npc.setCountry(MapCountry.valueOf(countryId.getValue()));
		npc.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				Integer skillId = CountryCopyConfig.getInstance().KILL_MONSTER_GAIN_SKILL.getValue().get(
						npc.getSpawnKey());
				if (skillId == null) {
					SM_TechCopy_MonsterHunt sm = SM_TechCopy_MonsterHunt.valueOf();
					Iterator<Player> playerIterator = worldMapInstance.playerIterator();
					while (playerIterator.hasNext()) {
						PacketSendUtility.sendPacket(playerIterator.next(), sm);
					}
					return;
				}
				SkillResource skillRes = SkillManager.getInstance().getResource(skillId);
				int range = skillRes.getRange();

				SM_TechCopy_MonsterHunt sm = SM_TechCopy_MonsterHunt.valueOf();
				Iterator<Player> playerIterator = worldMapInstance.playerIterator();
				while (playerIterator.hasNext()) {
					Player p = playerIterator.next();
					boolean isInRange = MathUtil.isInRange(npc, p, range, range);
					if (isInRange) {
						Skill skill = SkillEngine.getInstance().getSkill(npc, skillId, p.getObjectId(), 0, 0,
								(Creature) p, null);
						skill.noEffectorUseSkill();
					}
					PacketSendUtility.sendPacket(p, sm);
				}
			}
		});
		visibleObjects.add(npc);
		SpawnManager.getInstance().bringIntoWorld(npc, countryId.getValue());

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				clearMonsters();
			}
		}, CountryCopyConfig.getInstance().MONSTER_DELETE_DURATION.getValue() * DateUtils.MILLIS_PER_SECOND);
	}

	private void clearBoss() {
		if (boss != null) {
			boss.getController().delete();
			boss = null;
		}
	}

	private void clearMonsters() {
		if (visibleObjects != null && !visibleObjects.isEmpty()) {
			for (VisibleObject visObj : visibleObjects) {
				visObj.getController().delete();
			}
			visibleObjects.clear();
		}
	}

	public ArrayList<TechCopyRankVO> doReward(Creature creature) {
		ArrayList<TechCopyRankVO> rankVOs = New.arrayList();
		Map<Integer, Player> ranks = ((CountryCopyBossController) boss.getController()).getDamageRank();
		Map<Player, Long> damages = ((CountryCopyBossController) boss.getController()).getDamages();
		for (Entry<Integer, Player> rank : ranks.entrySet()) {
			// 构建奖励,邮件
			List<String> factors = ChooserManager.getInstance().chooseValueByRequire(rank.getKey(),
					CountryCopyConfig.getInstance().TECH_DAMAGE_FACTOR_CHOOSERGROUP.getValue());
			Map<String, Object> params = New.hashMap();
			params.put("DAMAGE_FACTOR", Double.valueOf(factors.get(0)));
			params.put("LEVEL", rank.getValue().getLevel());
			params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(rank.getValue()));
			params.put("STANDARD_COINS", PlayerManager.getInstance().getStandardCoins(rank.getValue()));
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(rank.getValue(),
					CountryCopyConfig.getInstance().TECH_DAMAGE_REWARD_CHOOSERGROUP.getValue());
			Reward reward = RewardManager.getInstance().creatReward(rank.getValue(), rewardIds, params);
			I18nUtils titel18n = I18nUtils.valueOf(CountryCopyConfig.getInstance().TECH_DAMAGE_MAIL_TITLE.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(CountryCopyConfig.getInstance().TECH_DAMAGE_MAIL_CONTENT
					.getValue());
			contextl18n.addParm("rank", I18nPack.valueOf(rank.getKey()));
			Long damage = damages.get(rank.getValue());
			TechCopyRankVO vo = TechCopyRankVO.valueOf(rank.getKey(), damage, rank.getValue(), reward);
			if (rank.getKey() <= 3) { // config
				rankVOs.add(vo);
			}
			rewardStatus.put(rank.getValue().getObjectId(), vo);
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, rank.getValue().getObjectId());
		}
		if (creature == null) {
			return rankVOs;
		}
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				CountryCopyConfig.getInstance().TECH_LAST_ATTACK_REWARD_CHOOSERGROUP.getValue());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
		I18nUtils titel18n = I18nUtils.valueOf(CountryCopyConfig.getInstance().TECH_LAST_ATTACK_MAIL_TITLE.getValue());
		I18nUtils contextl18n = I18nUtils.valueOf(CountryCopyConfig.getInstance().TECH_LAST_ATTACK_MAIL_CONTENT
				.getValue());
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
		MailManager.getInstance().sendMail(mail, player.getObjectId());
		return rankVOs;
	}

	public void doSendCopyStatus() {
		if (boss == null) {
			if (noticeFuture != null) {
				noticeFuture.cancel(false);
			}
			return;
		}

		SM_TechCopy_Status sm = SM_TechCopy_Status.valueOf(boss);
		Iterator<Player> playerIterator = worldMapInstance.playerIterator();
		while (playerIterator.hasNext()) {
			PacketSendUtility.sendPacket(playerIterator.next(), sm);
		}
	}

	public boolean isWarring() {
		return warring;
	}

	public long getEndTime() {
		return endTime;
	}

	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	public void setWorldMapInstance(WorldMapInstance worldMapInstance) {
		this.worldMapInstance = worldMapInstance;
	}

	public long getNextMonsterSpawnTime() {
		return nextSpawnTime;
	}

	public String getNextSpawnKey() {
		return nextSpawnKey;
	}

	public Map<Long, TechCopyRankVO> getRewardRanks() {
		return rewardStatus;
	}
}
