package com.mmorpg.mir.model.kingofwar.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.ForbidChat;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.country.packet.SM_Country_ForbidChat;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.controller.KingOfWarBigBossController;
import com.mmorpg.mir.model.kingofwar.controller.KingOfWarGuardController;
import com.mmorpg.mir.model.kingofwar.controller.KingOfWarPlayerController;
import com.mmorpg.mir.model.kingofwar.controller.KingOfWarStatusNpcController;
import com.mmorpg.mir.model.kingofwar.entity.KingOfWarEnt;
import com.mmorpg.mir.model.kingofwar.event.KingOfKingAbdicateEvent;
import com.mmorpg.mir.model.kingofwar.model.KingOfWarInfo;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.kingofwar.model.Sculptures;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_CommandCount;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_End;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_EndList;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Info;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_KingOfKing;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Status;
import com.mmorpg.mir.model.kingofwar.packet.vo.BossHpVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.CommandPlayerVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.KingCommandVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerRankInfoVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.ReliveStatusVO;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.effecttemplate.ForbidChatEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.transport.resource.PlayerTransportResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

/**
 * 王城
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-4
 * 
 */
@Component
public class KingOfWarManager {
	private static Logger logger = Logger.getLogger(KingOfWarManager.class);

	private static KingOfWarManager instance;

	@PostConstruct
	public void init() {
		setInstance(this);
	}

	/** 所有的BOSS */
	private List<Boss> bosses = New.arrayList();
	/** 据点NPC */
	private Map<String, StatusNpc> statusNpcs = New.hashMap();
	/** 所有的可视物 */
	private List<VisibleObject> visibleObjects = new CopyOnWriteArrayList<VisibleObject>();

	@Autowired
	private KingOfWarConfig kingOfWarConfig;

	@Autowired
	private SpawnManager spawnManager;
	/** 战斗进行中 */
	private volatile boolean warring;
	/** 开始时间 */
	private long startTime;
	/** 结束任务 */
	private Future<?> endFuture;
	/** 首杀任务 */
	public Map<CountryId, Boolean> firstKills = New.hashMap();
	/** 无敌任务 */
	private Future<?> godFuture;
	/** 排名刷新任务 */
	private Future<?> rankFuture;
	/** 清理排行任务 */
	private Future<?> clearTempRankFuture;
	/** 玩家战斗信息 */
	private Map<Long, PlayerWarInfo> playerWarInfos = New.concurrentHashMap();
	/** 每个国家在线的玩家数量缓存 */
	private Map<Integer, Integer> playerCounts = New.hashMap();
	/** 国王指挥集结点 */
	private Map<CountryId, String> kingCommands = New.hashMap();

	@Static
	private Storage<Integer, PlayerTransportResource> ptraStorage;

	@Autowired
	private ChatManager chatManager;

	@Inject
	private EntityCacheService<Integer, KingOfWarEnt> kingOfWarEntEntDbService;

	private KingOfWarInfo kingOfWarInfo;

	public int reliveSkillId;

	public String reliveStack;

	@Autowired
	private SkillEngine skillEngine;

	@Autowired
	private MailManager mailManager;

	private Sculptures scupltures = new Sculptures();

	private boolean mergeFirst;

	@PostConstruct
	public void initAll() {
		KingOfWarEnt ent = kingOfWarEntEntDbService.loadOrCreate(1, new EntityBuilder<Integer, KingOfWarEnt>() {
			@Override
			public KingOfWarEnt newInstance(Integer id) {
				KingOfWarEnt ent = new KingOfWarEnt();
				ent.setKingOfWarInfoJson(JsonUtils.object2String(KingOfWarInfo.valueOf()));
				ent.setId(id);
				return ent;
			}
		});

		kingOfWarInfo = ent.getKingOfWarInfo();
		kingOfWarInfo.setUpdate(kingOfWarEntEntDbService, ent);
		this.reliveStack = skillEngine.loadOrCreateSkillTemplate(kingOfWarConfig.RELIVE_BUFF_SKILL.getValue())
				.getGroup();
		this.reliveSkillId = skillEngine.loadOrCreateSkillTemplate(kingOfWarConfig.RELIVE_BUFF_SKILL.getValue())
				.getSkillId();
	}

	public void initSculptures() {
		scupltures.initKingSculpture();
	}

	public void refreshSculptures(int countryValue) {
		scupltures.refreshKingScalepture(countryValue);
	}

	public List<Sculpture> getCountrySculpture(int countryValue) {
		return scupltures.getKingScalpture().get(countryValue);
	}

	/**
	 * 开始！
	 */
	synchronized public void start(boolean mergeFirst) {
		if (isWarring()) {
			return;
		}
		// 重置
		reset();

		// 构建地图
		MapInstanceService.createOrLoadGangOfWarMapCopy(kingOfWarConfig.MAPID.getValue(), 1);

		// 大将军王
		Boss bigBoss = (Boss) spawnManager.spawnObject(kingOfWarConfig.BIG_BOSS_SPAW.getValue(), 1,
				new KingOfWarBigBossController(this));
		bosses.add(bigBoss);
		// 设置无敌
		bigBoss.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		getVisibleObjects().add(bigBoss);

		// 复活点的状态NPC
		VisibleObject centerStatusNpc = spawnManager.creatObject(kingOfWarConfig.CENTER_STATUSNPC_SPAW.getValue(), 1,
				new KingOfWarStatusNpcController());
		VisibleObject leftStatusNpc = spawnManager.creatObject(kingOfWarConfig.LEFT_STATUSNPC_SPAW.getValue(), 1,
				new KingOfWarStatusNpcController());
		VisibleObject rightStatusNpc = spawnManager.creatObject(kingOfWarConfig.RIGHT_STATUSNPC_SPAW.getValue(), 1,
				new KingOfWarStatusNpcController());
		getVisibleObjects().add(centerStatusNpc);
		getVisibleObjects().add(leftStatusNpc);
		getVisibleObjects().add(rightStatusNpc);
		getStatusNpcs().put(centerStatusNpc.getSpawnKey(), (StatusNpc) centerStatusNpc);
		getStatusNpcs().put(leftStatusNpc.getSpawnKey(), (StatusNpc) leftStatusNpc);
		getStatusNpcs().put(rightStatusNpc.getSpawnKey(), (StatusNpc) rightStatusNpc);

		// 禁卫军
		VisibleObject centerGuard = spawnManager.spawnObject(kingOfWarConfig.CENTER_BOSS_SPAW.getValue(), 1,
				new KingOfWarGuardController(bigBoss, (StatusNpc) centerStatusNpc));
		VisibleObject leftGuard = spawnManager.spawnObject(kingOfWarConfig.LEFT_BOSS_SPAW.getValue(), 1,
				new KingOfWarGuardController(bigBoss, (StatusNpc) leftStatusNpc));
		VisibleObject rightGuard = spawnManager.spawnObject(kingOfWarConfig.RIGHT_BOSS_SPAW.getValue(), 1,
				new KingOfWarGuardController(bigBoss, (StatusNpc) rightStatusNpc));
		getVisibleObjects().add(centerGuard);
		getVisibleObjects().add(leftGuard);
		getVisibleObjects().add(rightGuard);
		bosses.add((Boss) centerGuard);
		bosses.add((Boss) leftGuard);
		bosses.add((Boss) rightGuard);

		for (Boss boss : bosses) {
			bossHpTemp.put(boss.getSpawnKey(), boss.getLifeStats().getMaxHp());
		}

		// 超时任务
		endFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				end(null);
			}
		}, kingOfWarConfig.END_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);

		// 排名任务
		rankFuture = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				rank();
			}
		}, 10000, 5000);

		startTime = System.currentTimeMillis();

		I18nUtils i18nUtils = I18nUtils.valueOf("301002");
		chatManager.sendSystem(0, i18nUtils, null);

		// 当前地图所有人自动进入战场
		for (WorldMapInstance instance : World.getInstance()
				.getWorldMap(KingOfWarConfig.getInstance().MAPID.getValue()).getInstances().values()) {
			Iterator<Player> players = instance.playerIterator();
			while (players.hasNext()) {
				Player player = players.next();
				playerJoin(player);
			}
		}

		// 超时任务
		godFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// 当前地图所有人自动进入战场
				for (WorldMapInstance instance : World.getInstance()
						.getWorldMap(KingOfWarConfig.getInstance().MAPID.getValue()).getInstances().values()) {
					Iterator<Player> players = instance.playerIterator();
					while (players.hasNext()) {
						Player player = players.next();
						player.getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
					}
				}
			}
		}, kingOfWarConfig.START_PROTECT_TIME.getValue());

		I18nUtils i18nUtils1 = I18nUtils.valueOf("401001");
		chatManager.sendSystem(7100180, i18nUtils1, null);

		ServerState.getInstance().setKingOfWarDate(new Date());
		kingOfWarInfo.addCount();
		setWarring(true);
		this.mergeFirst = mergeFirst;
	}

	/**
	 * 发送给在地图中，并且在线的玩家
	 * 
	 * @param packet
	 */
	public void sendPackOnWar(Object packet) {
		sendPackOnWar(packet, null);
	}

	/**
	 * 发送给在地图中，并且在线的国家玩家
	 * 
	 * @param packet
	 */
	public void sendPackOnWar(Object packet, Country country) {
		for (PlayerWarInfo info : playerWarInfos.values()) {
			if (info.getPlayer().getController() instanceof KingOfWarPlayerController) {
				if (SessionManager.getInstance().isOnline(info.getPlayer().getObjectId())) {
					if (country == null || info.getPlayer().getCountry() == country) {
						PacketSendUtility.sendPacket(info.getPlayer(), packet);
					}
				}
			}
		}
	}

	/** 活动中积分缓存 */
	private List<PlayerWarInfo> rankTemp = new ArrayList<PlayerWarInfo>();

	public ArrayList<PlayerRankInfoVO> getTopPlayerWarInfo(Player player) {
		ArrayList<PlayerRankInfoVO> rankInfo = new ArrayList<PlayerRankInfoVO>();
		int i = 1;
		for (PlayerWarInfo pi : getRankTemp()) {
			if (i > KingOfWarConfig.getInstance().POINTS_RANK_SIZE.getValue()) {
				break;
			}
			rankInfo.add(pi.createRankInfo());
			i++;
		}
		return rankInfo;
	}

	public ArrayList<PlayerRankInfoVO> getTopPlayerWarInfo(int start, int end) {
		ArrayList<PlayerRankInfoVO> rankInfo = new ArrayList<PlayerRankInfoVO>();
		for (int i = start; i < end; i++) {
			if (i >= rankTemp.size()) {
				break;
			}
			rankInfo.add(rankTemp.get(i).createRankInfo());
		}
		return rankInfo;
	}

	public ArrayList<CommandPlayerVO> getCommandPlayerVOs(Player player, int countryId) {
		if (!player.isKing()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_AUTHORITY_ERROR);
		}
		ArrayList<CommandPlayerVO> commandPlayerVOs = new ArrayList<CommandPlayerVO>();
		for (PlayerWarInfo pi : playerWarInfos.values()) {
			if (pi.getPlayer().getController() instanceof KingOfWarPlayerController) {
				if (pi.getPlayer().getCountryValue() == countryId) {
					if (SessionManager.getInstance().isOnline(pi.getPlayer().getObjectId())) {
						commandPlayerVOs.add(CommandPlayerVO.valueOf(pi.getPlayer()));
					}
				}
			}
		}
		return commandPlayerVOs;
	}

	/**
	 * 刷新排名
	 */
	synchronized private void rank() {
		List<PlayerWarInfo> rankTemp = new ArrayList<PlayerWarInfo>();
		for (PlayerWarInfo pi : playerWarInfos.values()) {
			rankTemp.add(pi);
		}
		Collections.sort(rankTemp);
		int rank = 1;
		for (PlayerWarInfo playerWarInfo : rankTemp) {
			if (playerWarInfo.getRank() != rank) {
				playerWarInfo.setRank(rank);
				playerWarInfo.sendUpdate();
			}
			rank++;
		}
		// 缓存起来便于查询
		this.rankTemp = rankTemp;
		// 发送boss血量变化,
		BossHpVO bossHp = BossHpVO.valueOf();
		for (Boss boss : bosses) {
			// BOSS没有变化就不发送
			if (bossHpTemp.get(boss.getSpawnKey()) != boss.getLifeStats().getCurrentHp()) {
				bossHp.add(boss.getSpawnKey(), boss.getLifeStats().getCurrentHp());
				bossHpTemp.put(boss.getSpawnKey(), boss.getLifeStats().getCurrentHp());
			}
		}
		if (!bossHp.getBossHps().isEmpty()) {
			sendPackOnWar(bossHp);
		}
	}

	/** BOSS 血量缓存,检查是否有变化 */
	private Map<String, Long> bossHpTemp = New.hashMap();

	/**
	 * 战斗结束
	 */
	synchronized public void end(Player killBigBoss) {
		if (!isWarring()) {
			return;
		}
		clearWarObject();
		startTime = 0;
		long endTime = System.currentTimeMillis();
		rank();
		PlayerSimpleInfo kingOfking = null;
		long originalKingId = kingOfWarInfo.getKingOfKing();
		if (killBigBoss != null) {
			// 大将军被击杀
			Official kingOfficial = killBigBoss.getCountry().getCourt().getKing();
			if (kingOfficial != null) {
				long kingId = kingOfficial.getPlayerId();
				Player king = PlayerManager.getInstance().getPlayer(kingId);
				if (kingOfWarInfo.getKingOfKing() != kingId) {
					kingOfWarInfo.becomeKing(king);
				}
				king.getHorse().getAppearance().sendNotFinishActive(king);
				kingOfking = king.createSimple();
				PacketSendUtility.broadcastPacket(king, SM_KingOfWar_KingOfKing.valueOf(king.getObjectId()), true);

				I18nUtils i18nUtils = I18nUtils.valueOf("402002");
				i18nUtils.addParm(I18NparamKey.KING, I18nPack.valueOf(king.getName()));
				i18nUtils.addParm("country", I18nPack.valueOf(king.getCountry().getName()));
				chatManager.sendSystem(71001, i18nUtils, null);
			} else {
				kingOfWarInfo.setKingOfKing(0);
			}
		} else {
			kingOfWarInfo.setKingOfKing(0);
		}

		if (kingOfWarInfo.getKingOfKing() == 0L) {
			// 没有皇帝产生
			I18nUtils i18nUtils = I18nUtils.valueOf("402003");
			ChatManager.getInstance().sendSystem(71001, i18nUtils, null);
		}

		if (originalKingId != kingOfWarInfo.getKingOfKing()) {
			if (originalKingId != 0L) {
				Player oldKing = PlayerManager.getInstance().getPlayer(originalKingId);
				if (ModuleOpenManager.getInstance().isOpenByModuleKey(oldKing, ModuleKey.HORSE)) {
					oldKing.getGameStats().replaceModifiers(Horse.GAME_STATE_ID, oldKing.getHorse().getStat(), true);
				}
				EventBusManager.getInstance().submit(KingOfKingAbdicateEvent.valueOf(originalKingId));
			}
			scupltures.refreshKingScalepture(0); // 皇帝发生变化，刷新皇帝雕像
			KingOfWarManager.getInstance().refreshKingsWarship();
		}

		// 邮件发送皇帝每日礼包
		rewardKingDaily();
		// 结算发奖,建议都走邮件
		List<PlayerWarInfo> playerRanks = New.arrayList();
		// 顺便找出BOSS伤害,kill第一的玩家
		PlayerWarInfo damageFirst = null;
		PlayerWarInfo killFirst = null;
		for (PlayerWarInfo playerWarInfo : playerWarInfos.values()) {
			if (damageFirst == null || damageFirst.getBossDamage() < playerWarInfo.getBossDamage()) {
				damageFirst = playerWarInfo;
			}
			if (killFirst == null || killFirst.getAllKillCount() < playerWarInfo.getAllKillCount()) {
				killFirst = playerWarInfo;
			}
			playerRanks.add(playerWarInfo);
		}
		Collections.sort(playerRanks);
		this.rankTemp = playerRanks;
		int i = 1;
		PlayerWarInfo pointsFirst = null;
		for (PlayerWarInfo player : playerRanks) {
			try {
				Reward reward = Reward.valueOf();
				Map<String, Object> parms = new HashMap<String, Object>();
				parms.put("LEVEL", player.getPlayer().getLevel());
				parms.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player.getPlayer()));
				int rankRewardLeastPoint = kingOfWarConfig.KING_OF_WAR_END_REWARD_LEAST_POINT.getValue();
				int attendRewardLeastPoint = kingOfWarConfig.KING_OF_WAR_ATTEND_REWARD_LEAST_POINT.getValue();
				if (player.getPoints() >= rankRewardLeastPoint) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player.getPlayer(),
							KingOfWarConfig.getInstance().END_REWARD_CHOOSER.getValue());
					Reward rankReward = RewardManager.getInstance().creatReward(player.getPlayer(), rewardIds, parms);
					reward.addReward(rankReward);
				} else if (player.getPoints() >= attendRewardLeastPoint && player.getPoints() < rankRewardLeastPoint) {
					Reward attendReward = RewardManager.getInstance().creatReward(player.getPlayer(),
							kingOfWarConfig.KING_OF_WAR_ATTEND_REWARD_ID.getValue(), parms);
					reward.addReward(attendReward);
				}
				if (player.getPlayer().getMilitary().getRank() >= KingOfWarConfig.getInstance().MILITART_REWARD_LEVEL
						.getValue()) {
					Reward military = reward.divideIntoTwoPieces(
							KingOfWarConfig.getInstance().MILITART_REWARD_VALUE.getValue() * 1.0 / 100,
							ExpressManager.getInstance().ROB_SHARE_TYPE.getValue());
					reward.addReward(military);
				}

				I18nUtils titel18n = I18nUtils.valueOf(KingOfWarConfig.getInstance().END_MAIL_TITLE.getValue());
				I18nUtils contextl18n = I18nUtils.valueOf(KingOfWarConfig.getInstance().END_MAIL_CONTENT.getValue());
				contextl18n.addParm("country", I18nPack.valueOf(player.getPlayer().getCountry().getName()));
				contextl18n.addParm("n", I18nPack.valueOf(i + ""));
				contextl18n.addParm("kow_point", I18nPack.valueOf(player.getPoints() + ""));
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				List<String> countryNpcSender = ChooserManager.getInstance().chooseValueByRequire(player.getPlayer(),
						KingOfWarConfig.getInstance().MAIL_SENDER_NPC_CHOOSER.getValue());
				mail.setNpcId(countryNpcSender.get(0));
				mailManager.sendMail(mail, player.getPlayer().getObjectId());
				if (i == 1) {
					pointsFirst = player;
				}
				// send endList
				if (SessionManager.getInstance().isOnline(player.getPlayer().getObjectId())) {
					PacketSendUtility.sendPacket(player.getPlayer(), SM_KingOfWar_EndList.valueOf(endTime, player
							.createVO(), pointsFirst.getPlayer().createSimple(), killFirst.getPlayer().createSimple(),
							damageFirst.getPlayer().createSimple(), kingOfking, reward));
				}
				i++;

				if (kingOfWarInfo.getOpenCount() == 1) {
					String rewardId = null;
					String titleId = null;
					String contentId = null;
					if (kingOfWarInfo.getKingOfKing() == player.getPlayer().getObjectId()) {
						titleId = kingOfWarConfig.KING_ADDITION_REWARD_TITLE_IL18N.getValue();
						contentId = kingOfWarConfig.KING_ADDITION_REWARD_CONTENT_IL18N.getValue();
						rewardId = kingOfWarConfig.KING_ADDITION_REWARD.getValue();
					} else if (kingOfking != null && kingOfking.getCountry() == player.getPlayer().getCountryValue()) {
						titleId = kingOfWarConfig.WIN_ADDITION_REWARD_TITLE_IL18N.getValue();
						contentId = kingOfWarConfig.WIN_ADDITION_REWARD_CONTENT_IL18N.getValue();
						rewardId = kingOfWarConfig.WIN_ADDITION_REWARD.getValue();
					} else {
						titleId = kingOfWarConfig.OTHER_ADDITION_REWARD_TITLE_IL18N.getValue();
						contentId = kingOfWarConfig.OTHER_ADDITION_REWARD_CONTENT_IL18N.getValue();
						rewardId = kingOfWarConfig.OTHER_ADDITION_REWARD.getValue();
					}
					Reward addtionReward = RewardManager.getInstance().creatReward(player.getPlayer(), rewardId, null);
					I18nUtils titleIL18n = I18nUtils.valueOf(titleId);
					I18nUtils contextL18n = I18nUtils.valueOf(contentId);

					Mail additionMail = Mail.valueOf(titleIL18n, contextL18n, null, addtionReward);
					mailManager.sendMail(additionMail, player.getPlayer().getObjectId());
				}

				if (mergeFirst) {
					String rewardId = null;
					String titleId = null;
					String contentId = null;

					if (kingOfWarInfo.getKingOfKing() == player.getPlayer().getObjectId()) {
						titleId = MergeActiveConfig.getInstance().MERGE_KOW_KING_TITLE_IL18NID.getValue();
						contentId = MergeActiveConfig.getInstance().MERGE_KOW_KING_CONTENT_IL18NID.getValue();
						rewardId = MergeActiveConfig.getInstance().MERGE_KOW_KING_REWARDID.getValue();
					} else if (kingOfking != null && kingOfking.getCountry() == player.getPlayer().getCountryValue()) {
						titleId = MergeActiveConfig.getInstance().MERGE_KOW_WIN_EXCEPT_KING_TITLE_IL18NID.getValue();
						contentId = MergeActiveConfig.getInstance().MERGE_KOW_WIN_EXCEPT_KING_CONTENT_IL18NID
								.getValue();
						rewardId = MergeActiveConfig.getInstance().MERGE_KOW_WIN_EXCEPT_KING_REWARDID.getValue();
					} else if (kingOfking != null && kingOfking.getCountry() != player.getPlayer().getCountryValue()) {
						titleId = MergeActiveConfig.getInstance().MERGE_KOW_LOSE_TITLE_IL18NID.getValue();
						contentId = MergeActiveConfig.getInstance().MERGE_KOW_LOSE_CONTENT_IL18NID.getValue();
						rewardId = MergeActiveConfig.getInstance().MERGE_KOW_LOSE_REWARDID.getValue();
					}

					if (rewardId != null) {
						Reward addtionReward = RewardManager.getInstance().creatReward(player.getPlayer(), rewardId,
								null);
						I18nUtils titleIL18n = I18nUtils.valueOf(titleId);
						I18nUtils contextL18n = I18nUtils.valueOf(contentId);

						Mail additionMail = Mail.valueOf(titleIL18n, contextL18n, null, addtionReward);
						mailManager.sendMail(additionMail, player.getPlayer().getObjectId());
					}
				}

			} catch (Exception e) {
				logger.error("KingOfWar end Error", e);
			}
		}

		playerWarInfos.clear();
		kingCommands.clear();
		playerCounts.clear();

		// 清理排名任务
		clearTempRankFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rankTemp.clear();
			}
		}, DateUtils.MILLIS_PER_HOUR / 2);
		setWarring(false);

		for (Long playerId : SessionManager.getInstance().getOnlineIdentities()) {
			PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId), SM_KingOfWar_End.valueOf());
		}
	}

	/**
	 * 重置战场
	 */
	public void reset() {
		clearWarObject();
		firstKills.clear();
		playerWarInfos.clear();
		kingCommands.clear();
		playerCounts.clear();
		rankTemp.clear();
		// 老皇帝下位
		// kingOfWarInfo.setKingOfKing(0);
	}

	/**
	 * 清理场上所有对象
	 */
	public void clearWarObject() {
		if (endFuture != null && !endFuture.isCancelled()) {
			endFuture.cancel(true);
		}
		if (rankFuture != null && !rankFuture.isCancelled()) {
			rankFuture.cancel(true);
		}
		if (godFuture != null && !godFuture.isCancelled()) {
			godFuture.cancel(true);
		}
		if (clearTempRankFuture != null && !clearTempRankFuture.isCancelled()) {
			clearTempRankFuture.cancel(true);
		}
		// 清理老的物件
		for (VisibleObject visibleObject : getVisibleObjects()) {
			visibleObject.getController().delete();
		}
		getVisibleObjects().clear();
		bosses.clear();
		bossHpTemp.clear();

	}

	/**
	 * 玩家加入
	 * 
	 * @param player
	 */
	private void playerJoin(Player player) {
		if (!getPlayerWarInfos().containsKey(player.getObjectId())) {
			getPlayerWarInfos().put(player.getObjectId(), PlayerWarInfo.valueOf(player));
		}
		if (isProtect()) {
			player.getEffectController().setAbnormal(EffectId.GOD.getEffectId(), true);
		}
		getPlayerWarInfos().get(player.getObjectId()).refreshBombBuff();
		player.getMilitary().buttonTheWarActivityStat(true);
		// 推送战场信息
		PacketSendUtility.sendPacket(player, createWarInfo(player));
		// 推送人数信息
		sendPlayerCount();
	}

	public void sendPlayerCount() {
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			if (!playerCounts.containsKey(country.getId().getValue())) {
				playerCounts.put(country.getId().getValue(), 0);
			}
			int count = 0;
			for (PlayerWarInfo pi : playerWarInfos.values()) {
				if (pi.getPlayer().getController() instanceof KingOfWarPlayerController) {
					if (pi.getPlayer().getCountryValue() == country.getId().getValue()) {
						if (SessionManager.getInstance().isOnline(pi.getPlayer().getObjectId())) {
							count++;
						}
					}
				}
			}
			playerCounts.put(country.getId().getValue(), count);
			if (country.getKing() != null) {
				if (country.getKing().getController() instanceof KingOfWarPlayerController) {
					if (SessionManager.getInstance().isOnline(country.getKing().getObjectId())) {
						PacketSendUtility
								.sendPacket(country.getKing(), SM_KingOfWar_CommandCount.valueOf(playerCounts));
					}
				}
			}
		}
	}

	/**
	 * 构建当前战场信息
	 * 
	 * @param player
	 * @return
	 */
	private SM_KingOfWar_Info createWarInfo(Player player) {
		SM_KingOfWar_Info info = new SM_KingOfWar_Info();
		info.setEndTime(startTime + kingOfWarConfig.END_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
		info.setPlayerWarInfoVO(getPlayerWarInfos().get(player.getObjectId()).createVO());
		info.setCurrentHp(BossHpVO.valueOf());
		info.setMaxHp(BossHpVO.valueOf());
		for (Boss boss : bosses) {
			info.getCurrentHp().add(boss.getSpawnKey(), boss.getLifeStats().getCurrentHp());
			info.getMaxHp().add(boss.getSpawnKey(), boss.getLifeStats().getMaxHp());
		}
		info.setReliveStatus(ReliveStatusVO.valueOf(statusNpcs));
		info.setCommands(KingCommandVO.valueOf(kingCommands.get(player.getCountryId())));
		return info;
	}

	/**
	 * 构建玩家切换地图的观察者
	 * 
	 * @param player
	 * @return
	 */
	public ActionObserver kingOfWarSpawnObsever(final Player player) {
		ActionObserver observer = new ActionObserver(ObserverType.SPAWN) {
			@Override
			public void spawn(int mapId, int instanceId) {
				// 切换地图处理
				if (mapId == KingOfWarConfig.getInstance().MAPID.getValue()) {
					if (!isWarring()) {
						// 非战时,传送到咸阳城那里去
						World.getInstance().setPosition(player, kingOfWarConfig.XIANYANG_MAPID.getValue(),
								World.INIT_INSTANCE, player.getX(), player.getY(), player.getHeading());
						player.sendUpdatePosition();
					} else {
						// 进入地图
						if (!(player.getController() instanceof KingOfWarPlayerController)) {
							player.setController(new KingOfWarPlayerController());
							player.getController().setOwner(player);
						}
						playerJoin(player);
						// 检查复活点BUFF
						for (StatusNpc npc : statusNpcs.values()) {
							if (npc.getStatus() == player.getCountryValue()) {
								addReliveBuff(player);
							}
						}
					}
				} else {
					// 离开地图
					if (player.getController() instanceof KingOfWarPlayerController) {
						player.setController(new PlayerController());
						player.getController().setOwner(player);
						removeReliveBuff(player);
						player.getEffectController().removeEffect(
								KingOfWarConfig.getInstance().BOMB_BUFF_SKILL.getValue());
						// 这里要考虑要进入其它领域时，所要触发的
						if (player.getEffectController().isAbnoramlSet(EffectId.GOD)) {
							player.getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
						}
						player.getMilitary().buttonTheWarActivityStat(false);
						// 推送人数信息
						sendPlayerCount();
					}
				}
			}
		};
		player.getObserveController().addObserver(observer);
		return observer;
	}

	/**
	 * 添加复活点BUFF
	 * 
	 * @param player
	 */
	public void addReliveBuff(Player player) {
		if (!player.getEffectController().contains(reliveStack)) {
			// 释放加层BUFF
			Skill skill = SkillEngine.getInstance().getSkill(null,
					KingOfWarConfig.getInstance().RELIVE_BUFF_SKILL.getValue(), player.getObjectId(), 0, 0, player,
					null);
			skill.noEffectorUseSkill();
		}
	}

	/**
	 * 移除复活点BUFF
	 * 
	 * @param player
	 */
	public void removeReliveBuff(Player player) {
		if (player.getEffectController().contains(reliveStack)) {
			// 释放加层BUFF
			player.getEffectController().removeEffect(reliveSkillId);
		}
	}

	/**
	 * 是否处于开场保护阶段
	 * 
	 * @return
	 */
	public boolean isProtect() {
		if ((startTime + kingOfWarConfig.START_PROTECT_TIME.getValue()) > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public void enterWar(Player player) {
		if (!isWarring()) {
			return;
		}
		int mapId = KingOfWarConfig.getInstance().MAPID.getValue();
		if (player.getMapId() == mapId) {
			return;
		}
		if (!CoreConditionManager.getInstance()
				.getCoreConditions(1, KingOfWarConfig.getInstance().ENTER_CONDITIONS.getValue()).verify(player, true)) {
			return;
		}
		if (player.getPosition().getMapRegion().getParent().getParent().isCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		Integer[] bornPosition = KingOfWarConfig.getInstance().BORN_POSITION.getValue();
		World.getInstance().setPosition(player, mapId, World.INIT_INSTANCE, bornPosition[0], bornPosition[1],
				player.getHeading());
		player.sendUpdatePosition();
	}

	public List<Player> getPlayerOnWar(int country) {
		List<Player> players = New.arrayList();
		for (PlayerWarInfo pi : playerWarInfos.values()) {
			if (pi.getPlayer().getController() instanceof KingOfWarPlayerController) {
				if (pi.getPlayer().getCountryValue() == country) {
					// 在地图内
					players.add(pi.getPlayer());
				}
			}
		}
		return players;
	}

	public void leaveWar(Player player) {
		if (player.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
			return;
		}
		player.getMoveController().stopMoving();
		if (warring) {
			RouteStep position = player.getCopyHistory().getRouteStep();
			if (position != null) {
				World.getInstance().setPosition(player, position.getMapId(), position.getX(), position.getY(),
						player.getHeading());
			} else {
				// 防止出错让他回新手村
				List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryValue(),
						ConfigValueManager.getInstance().BIRTH_POINT.getValue());
				RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
				World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), (byte) 0);
			}
		} else {
			int id = Integer.valueOf(ChooserManager.getInstance().chooseValueByRequire(player, "KOW_END_FLYID").get(0));
			PlayerTransportResource tr = ptraStorage.get(id, true);
			Position destination = tr.selectPosition();
			if (player.getMapId() == tr.getTargetMapId()) {
				if (tr.getInstance() != 0
						&& player.getInstanceId() != tr.getInstance()
						&& World.getInstance().getWorldMap(tr.getTargetMapId())
								.getWorldMapInstanceById(tr.getInstance()) != null) {
					World.getInstance().setPosition(player, tr.getTargetMapId(), tr.getInstance(), destination.getX(),
							destination.getY(), player.getHeading());
				} else {
					World.getInstance().updatePosition(player, destination.getX(), destination.getY(),
							player.getHeading());
				}
			} else {
				if (tr.getInstance() != 0
						&& World.getInstance().getWorldMap(tr.getTargetMapId())
								.getWorldMapInstanceById(tr.getInstance()) != null) {
					World.getInstance().setPosition(player, tr.getTargetMapId(), tr.getInstance(), destination.getX(),
							destination.getY(), player.getHeading());
				} else {
					World.getInstance().setPosition(player, tr.getTargetMapId(), destination.getX(),
							destination.getY(), player.getHeading());
				}
			}
		}
		player.sendUpdatePosition();
	}

	public long getNextKingOfWarTime() {
		Date lastMergeTime = ServerState.getInstance().getServerEnt().getLastMergeTime();
		if (lastMergeTime != null) {
			int calcIntervalDays = DateUtils.calcIntervalDays(lastMergeTime, new Date());
			if (calcIntervalDays < KingOfWarConfig.getInstance().MERGE_DATE_NOT_OPEN.getValue()) {
				Date nextNotKingTime = DateUtils.getNextTime(KingOfWarConfig.getInstance().START_TIME.getValue(),
						new Date(DateUtils.getFirstTime(new Date()).getTime() + 1 + DateUtils.MILLIS_PER_DAY
								* (KingOfWarConfig.getInstance().MERGE_DATE_NOT_OPEN.getValue() - calcIntervalDays)));
				return nextNotKingTime.getTime();
			} else if (calcIntervalDays == KingOfWarConfig.getInstance().MERGE_DATE_NOT_OPEN.getValue()) {
				Date nextNotKingTime = DateUtils.getNextTime(KingOfWarConfig.getInstance().START_TIME.getValue(),
						new Date());
				boolean today = DateUtils.isToday(nextNotKingTime);
				if (today) {
					return nextNotKingTime.getTime();
				}
			}
		}

		if (ServerState.getInstance().getLastKingOfWarDate() == null) {
			return getOpenServerNextKingOfWarTime();
		} else {
			return DateUtils.getNextTime(kingOfWarConfig.START_TIME_WEEK.getValue(), new Date()).getTime();
		}
	}

	/**
	 * 开服的第3天开始皇城战
	 * 
	 * @return
	 */
	public long getOpenServerNextKingOfWarTime() {
		if (ServerState.getInstance().getOpenServerDate() == null) {
			return 0;
		}
		long open24Hour = DateUtils.getNextTime("0 0 0 * * *", ServerState.getInstance().getOpenServerDate()).getTime()
				+ DateUtils.MILLIS_PER_DAY;
		return DateUtils.getNextTime(KingOfWarConfig.getInstance().START_TIME.getValue(), new Date(open24Hour))
				.getTime();
	}

	public SM_KingOfWar_Status getKingOfWarStatus() {
		PlayerSimpleInfo kingOfKing = null;
		if (kingOfWarInfo.getKingOfKing() != 0) {
			kingOfKing = PlayerManager.getInstance().getPlayer(kingOfWarInfo.getKingOfKing()).createSimple();
		}
		long nextTime = getNextKingOfWarTime();
		SM_KingOfWar_Status sm = SM_KingOfWar_Status.valueOf(warring, kingOfKing, kingOfWarInfo.getBecomeKingTime(),
				nextTime, kingOfWarInfo);
		return sm;
	}

	private Map<CountryId, Long> commandCD = New.hashMap();

	public void setKingCommand(Player player, String command) {
		if (!warring) {
			return;
		}
		if (!player.isKing()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_AUTHORITY_ERROR);
		}
		if (commandCD.containsKey(player.getCountryId())) {
			if (commandCD.get(player.getCountryId()) > System.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_COMMAND_CD);
			}
		}
		Npc commandTarget = null;
		for (VisibleObject vo : visibleObjects) {
			if (vo.getSpawnKey().equals(command)) {
				commandTarget = (Npc) vo;
			}
		}
		if (commandTarget != null) {
			I18nUtils i18nUtils = I18nUtils.valueOf("10112");
			i18nUtils.addParm("guard", I18nPack.valueOf(commandTarget.getObjectResource().getName()));
			chatManager.sendSystem(11009, i18nUtils, null, kingOfWarConfig.MAPID.getValue(), player.getCountry());

			// I18nUtils i18nUtils1 = I18nUtils.valueOf("302002");
			// i18nUtils1.addParm("guard",
			// I18nPack.valueOf(commandTarget.getObjectResource().getName()));
			// i18nUtils1.addParm("mapId",
			// I18nPack.valueOf(commandTarget.getSpawn().getMapId()));
			// i18nUtils1.addParm("x",
			// I18nPack.valueOf(commandTarget.getSpawn().getX()));
			// i18nUtils1.addParm("y",
			// I18nPack.valueOf(commandTarget.getSpawn().getY()));
			// chatManager.sendSystem(9, i18nUtils1, null,
			// kingOfWarConfig.MAPID.getValue(), player.getCountry());
		}

		kingCommands.put(player.getCountryId(), command);
		commandCD.put(player.getCountryId(), System.currentTimeMillis() + DateUtils.MILLIS_PER_SECOND * 9);
		sendPackOnWar(KingCommandVO.valueOf(command), player.getCountry());
	}

	public boolean isKingOfKing(long playerId) {
		long kingId = kingOfWarInfo.getKingOfKing();
		if (kingId == 0L) {
			return false;
		}
		return kingOfWarInfo.getKingOfKing() == playerId;
	}

	public Player getKingOfKing() {
		long kingOfKingId = kingOfWarInfo.getKingOfKing();
		if (kingOfKingId != 0L) {
			return PlayerManager.getInstance().getPlayer(kingOfKingId);
		}
		return null;
	}

	public long getBecomeKingOfKingTime() {
		return kingOfWarInfo.getBecomeKingTime();
	}

	public static KingOfWarManager getInstance() {
		return instance;
	}

	public static void setInstance(KingOfWarManager instance) {
		KingOfWarManager.instance = instance;
	}

	public Map<String, StatusNpc> getStatusNpcs() {
		return statusNpcs;
	}

	public List<VisibleObject> getVisibleObjects() {
		return visibleObjects;
	}

	public Map<Long, PlayerWarInfo> getPlayerWarInfos() {
		return playerWarInfos;
	}

	public boolean isWarring() {
		return warring;
	}

	public void setWarring(boolean warring) {
		this.warring = warring;
	}

	public List<PlayerWarInfo> getRankTemp() {
		return rankTemp;
	}

	/**
	 * 禁言
	 * 
	 * @param player
	 * @param target
	 * @param sign
	 */
	public void forbidChat(Player player, Player target, int sign) {
		if (!player.isKingOfking()) {
			throw new ManagedException(ManagedErrorCode.KINGOFKING_NOT);
		}
		if (player.getObjectId() == target.getObjectId()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_CANTNOT_FORBID_SELF);
		}
		if (target.getCountry().isForbidchat(target.getObjectId())) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_FORBIDCHATED);
			return;
		}
		target.getCountry().forbidChat(target.getObjectId(),
				ConfigValueManager.getInstance().COUNTRY_FORBIDCHAT_TIME.getValue());
		if (SessionManager.getInstance().isOnline(target.getObjectId())) {
			ForbidChat chat = target.getCountry().getForbidChats().get(target.getObjectId());
			PacketSendUtility.sendPacket(target, SM_Country_ForbidChat.valueOf(chat.getEndTime()));
		}

		if (!target.getEffectController().contains(ForbidChatEffect.FORBIDCHAT)) {
			Skill skill = SkillEngine.getInstance().getSkill(null, 20069, target.getObjectId(), 0, 0, target, null);
			skill.noEffectorUseSkill();
		}

		I18nUtils utils = I18nUtils.valueOf("10505");
		utils.addParm("targetName", I18nPack.valueOf(target.getName()));
		utils.addParm("name", I18nPack.valueOf(player.getName()));
		utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, utils, null);

		PacketSendUtility.sendSignMessage(player, sign);
	}

	/**
	 * 皇帝每日奖励
	 */
	public void rewardKingDaily() {
		kingOfWarInfo.rewardKingDaily();
	}

	public long getStartTime() {
		return startTime;
	}

	public Map<CountryId, Long> getCommandCD() {
		return commandCD;
	}

	public void setCommandCD(Map<CountryId, Long> commandCD) {
		this.commandCD = commandCD;
	}

	public void refreshKingsWarship() {
		this.kingOfWarInfo.refresh();
	}

	public int getKingSupportCount() {
		return this.kingOfWarInfo.getSupportCount();
	}

	public int getKingContemptCount() {
		return this.kingOfWarInfo.getContemptCount();
	}

	public void supportKing() {
		this.kingOfWarInfo.setSupportCount(getKingSupportCount() + 1);
		kingOfWarInfo.update();
	}

	public void contemptKing() {
		this.kingOfWarInfo.setContemptCount(getKingContemptCount() + 1);
		kingOfWarInfo.update();
	}

	public KingOfWarInfo getKingOfWarInfo() {
		return kingOfWarInfo;
	}

	public void setKingOfWarInfo(KingOfWarInfo kingOfWarInfo) {
		this.kingOfWarInfo = kingOfWarInfo;
	}

}
