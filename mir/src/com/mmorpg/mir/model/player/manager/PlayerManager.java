package com.mmorpg.mir.model.player.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.ServerConfigValue;
import com.mmorpg.mir.model.artifact.core.ArtifactService;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.PlayerGameStats;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.entity.PlayerStat;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.event.ServerOpenEvent;
import com.mmorpg.mir.model.player.model.PlayerExpUpdate;
import com.mmorpg.mir.model.player.packet.SM_CheckExist;
import com.mmorpg.mir.model.player.packet.SM_LEVEL_UPDATE;
import com.mmorpg.mir.model.player.packet.SM_Server_Open;
import com.mmorpg.mir.model.player.resource.PlayerLevelResource;
import com.mmorpg.mir.model.player.resource.Role;
import com.mmorpg.mir.model.player.resource.SoulOfGeneralResource;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.soul.core.SoulService;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.transfer.packet.TransferAddress;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.CryptUtils;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.SelectRandom;
import com.windforce.common.utility.collection.ConcurrentHashSet;

/**
 * 玩家装配器管理器，这个管理器是用来管理底层数据，一般并不直接使用
 * 
 * @author liuzhou
 * 
 */
@Component
public class PlayerManager {

	@Inject
	private EntityCacheService<Long, PlayerEnt> playerDbService;
	@Inject
	private EntityCacheService<Long, PlayerStat> playerStatDbService;
	/** 模块初始化，反序列化处理器 */
	private Map<ModuleKey, ModuleHandle> initUpdateHandles = new HashMap<ModuleKey, ModuleHandle>();

	@Static("PLAYER:PK_DURATION_TIME")
	private ConfigValue<Integer> PK_DURATION_TIME;

	/** 标准攻击 */
	@Static("PLAYER:STANDARD_ATTACK")
	public ConfigValue<Integer> STANDARD_ATTACK;

	/** 标准暴击 */
	@Static("PLAYER:STANDARD_CRITICAL")
	public ConfigValue<Double> STANDARD_CRITICAL;

	@Static("PLAYER:STANDARD_HURT")
	public ConfigValue<Double> STANDARD_HURT;

	/** 标准防御 */
	@Static("PLAYER:STANDARD_DEFENSE")
	public ConfigValue<Integer> STANDARD_DEFENSE;

	/** 玩家每10增加的怒气量 */
	@Static("PLAYER:DP_INC_10SECOND")
	public ConfigValue<Integer> DP_INC_10SECOND;

	/** 注册玩家时，职业和国家人数达到该差值时干涉默认选择 */
	@Static("PLAYER:SELECT_PLAYER_COUNT")
	public ConfigValue<Integer> SELECT_PLAYER_COUNT;

	@Static("PUBLIC:STANDARD_EXPERIENCE")
	public ConfigValue<String> STANDARD_EXPERIENCE;

	@Static("PUBLIC:STANDARD_COINS")
	public ConfigValue<String> STANDARD_COINS;

	@Static("PUBLIC:STANDARD_INCR")
	public ConfigValue<String> STANDARD_INCR;

	@Static("PUBLIC:STANDARD_HONOR")
	public ConfigValue<String> STANDARD_HONOR;

	@Static("PUBLIC:JOB")
	public ConfigValue<String[]> JOB_NAMES;

	/** 客户端瞬移允许范围 */
	@Static("PUBLIC:CLIENT_SAFERANGE")
	public ConfigValue<Integer> CLIENT_SAFERANGE;

	/** 在监管NPC范围内，客户端瞬移允许范围 */
	@Static("PUBLIC:CLIENT_SAFERANGE_SUPERVISOR")
	public ConfigValue<Integer> CLIENT_SAFERANGE_SUPERVISOR;

	/** 客户端瞬移时间统计时间范围，毫秒(先尝试配置2秒内做5次判断，如果掉线的玩家太多了再调大) */
	@Static("PUBLIC:CLIENT_UPDATE_POSITION_TIME")
	public ConfigValue<Integer> CLIENT_UPDATE_POSITION_TIME;

	/** 客户端瞬移时间统计时间范围内瞬移的次数(先尝试配置2秒内做5次判断) */
	@Static("PUBLIC:CLIENT_UPDATE_POSITION_COUNT")
	public ConfigValue<Integer> CLIENT_UPDATE_POSITION_COUNT;

	/** 回城坐标 */
	@Static("PUBLIC:BACKHOME_POINT")
	public ConfigValue<String> BACKHOME_POINT;

	@Static("PUBLIC:PLAYER_GROUP_MODULE_KEY")
	public ConfigValue<String> PLAYER_GROUP_MODULE_KEY;

	@Static("PUBLIC:FRESHMAN_PROTECT")
	public ConfigValue<Integer> FRESHMAN_PROTECT;

	@Static("PUBLIC:DEAD_NOTICE_CONDITIONID")
	public ConfigValue<String[]> DEAD_NOTICE_CONDITIONID;

	@Static("PUBLIC:DEAD_NOTICE_CD_GROUP")
	public ConfigValue<Integer> DEAD_NOTICE_CD_GROUP;

	private CoreConditions coreConditions;

	@Static
	public Storage<Integer, PlayerLevelResource> playerLevelResource;

	@Static
	public Storage<Integer, SoulOfGeneralResource> soulOfGeneralResources;

	/*
	 * NOT SERVICE HERE.FIX THIS LATER.
	 */
	@Autowired
	private SoulService soulService;
	@Autowired
	private ArtifactService artifactService;

	private static PlayerManager self;

	public static PlayerManager getInstance() {
		return self;
	}

	private Set<String> indexSet = new ConcurrentHashSet<String>();

	private Set<String> nameSet = new ConcurrentHashSet<String>();

	// 处于跨服状态
	private Map<String, Long> indexSetTransferFlag = new ConcurrentHashMap<String, Long>();

	private Map<Integer, AtomicInteger> countryCounts = new HashMap<Integer, AtomicInteger>();

	private Map<Integer, AtomicInteger> roleCounts = new HashMap<Integer, AtomicInteger>();

	@Autowired
	private Querier querier;
	@Autowired
	private ServerConfigValue serverConfigValue;

	private SelectRandom<Integer> countrySelector = new SelectRandom<Integer>();
	private SelectRandom<Integer> roleSelector = new SelectRandom<Integer>();

	@PostConstruct
	public void init() {
		if (!ClearAndMigrate.clear) {
			List<String> list = querier.list(String.class, "PlayerEnt.listIndex");
			for (String index : list) {
				indexSet.add(index);
			}
			List<String> nameList = querier.list(String.class, "PlayerEnt.listName");
			for (String name : nameList) {
				nameSet.add(name);
			}

			// 初始化国家人数
			for (CountryId countryId : CountryId.values()) {
				List<Long> counts = querier.list(Long.class, "PlayerEnt.countryCount", countryId.getValue());
				long count = counts.get(0);
				countryCounts.put(countryId.getValue(), new AtomicInteger((int) count));
				countrySelector.addElement(countryId.getValue(), 1);
			}

			// 初始化职业数量
			for (Role role : Role.values()) {
				if (role == Role.SORCERER) {
					// 方士暂时不开放
					continue;
				}
				List<Long> counts = querier.list(Long.class, "PlayerEnt.roleCount", role.value());
				long count = counts.get(0);
				roleCounts.put(role.value(), new AtomicInteger((int) count));
				roleSelector.addElement(role.value(), 1);
			}
		}
		self = this;
	}

	public void transferAddress(String account, String op, String server, SM_CheckExist sm) {
		if (isTransferAddress(account, op, server)) {
			TransferAddress ta = TransferAddress.valueOf(serverConfigValue.getCenterDomain(),
					serverConfigValue.getCenterPort());
			sm.setTransferAddress(ta);
		}
	}

	private AtomicLong atomicLong = new AtomicLong(1);

	public void addTransferAddress(final Player player) {
		final long token = atomicLong.getAndIncrement();
		indexSetTransferFlag.put(player.getPlayerEnt().getAccountIndex(), token);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (indexSetTransferFlag.containsKey(player.getPlayerEnt().getAccountIndex())
						&& indexSetTransferFlag.get(player.getPlayerEnt().getAccountIndex()) == token) {
					indexSetTransferFlag.remove(player.getPlayerEnt().getAccountIndex());
				}
			}
		}, DateUtils.MILLIS_PER_SECOND * 10);
	}

	private boolean isTransferAddress(String account, String op, String server) {
		boolean result = indexSetTransferFlag.containsKey(account + "_" + op + "_" + server);
		indexSetTransferFlag.remove(account + "_" + op + "_" + server);
		return result;
	}

	public void logout(LogoutEvent event) {
		EventBusManager.getInstance().syncSubmit(event);
		this.updatePlayer(getPlayer(event.getOwner()));
	}

	public String getStandardExp(Player player) {
		List<String> stardardExp = ChooserManager.getInstance().chooseValueByRequire(player,
				STANDARD_EXPERIENCE.getValue());
		return stardardExp.get(0);
	}

	public String getStandardCoins(Player player) {
		List<String> standardCoins = ChooserManager.getInstance().chooseValueByRequire(player,
				STANDARD_COINS.getValue());
		return standardCoins.get(0);
	}

	public String getStandardHonor(Player player) {
		return STANDARD_HONOR.getValue();
	}

	public String getStandardIncr(Player player) {
		List<String> standardIncr = ChooserManager.getInstance().chooseValueByRequire(player, STANDARD_INCR.getValue());
		return standardIncr.get(0);
	}

	public boolean isIndexExist(String account, String op, String server) {
		return indexSet.contains(account + "_" + op + "_" + server);
	}

	public boolean isNameExist(String name) {
		return nameSet.contains(name);
	}

	public PlayerLevelResource getPlayerLevelResource(int id) {
		return this.playerLevelResource.get(id, true);
	}

	public Player createPlayer(String account, String playerName, int role, String server, String op, int country,
			final String source, final int noframe, final String userRefer) {
		long newId = IdentifyManager.getInstance().getNextIdentify(IdentifyType.PLAYER);
		final String newPlayerName = playerName;
		final int finalRole = role;
		final String newAccount = account;
		final String s = server;
		final String o = op;
		final CountryId countryId = CountryId.valueOf(country);
		PlayerEnt player = playerDbService.create(newId, new EntityBuilder<Long, PlayerEnt>() {
			@Override
			public PlayerEnt newInstance(Long id) {
				return PlayerEnt.valueOf(id, newPlayerName, finalRole, newAccount, o, s, countryId, source, noframe,
						userRefer);
			}
		});
		PlayerStat playerStat = playerStatDbService.create(newId, new EntityBuilder<Long, PlayerStat>() {
			@Override
			public PlayerStat newInstance(Long id) {
				return PlayerStat.valueOf(id);
			}

		});
		player.setStat(playerStat);

		indexSet.add(player.getAccountIndex());
		nameSet.add(player.getName());
		countryCounts.get(player.getCountry()).incrementAndGet();
		roleCounts.get(player.getRole()).incrementAndGet();

		if (!ServerState.getInstance().isOpenServer()) {
			if (nameSet.size() >= ConfigValueManager.getInstance().OPENSERVER_CREATEPLAYER_COUNT.getValue()) {
				// 开服
				Date now = new Date();
				ServerState.getInstance().setOpenServerDate(now);
				SM_Server_Open openPacket = SM_Server_Open.valueOf(now, player.getPlayer());
				SessionManager.getInstance().sendAllIdentified(openPacket);
				EventBusManager.getInstance().submit(new ServerOpenEvent());
			}
		}

		return getPlayer(newId);
	}

	public int selectCountry() {
		int min = Integer.MAX_VALUE;
		int minCountry = 0;

		int max = 0;
		for (Integer countryId : countryCounts.keySet()) {
			AtomicInteger count = countryCounts.get(countryId);
			if (min > count.get()) {
				minCountry = countryId;
				min = count.get();
			}
			if (max < count.get()) {
				max = count.get();
			}
		}

		return minCountry;
	}

	public int forbidSelectCountry() {
		int min = Integer.MAX_VALUE;
		int maxCountry = 0;

		int max = 0;
		for (Integer countryId : countryCounts.keySet()) {
			AtomicInteger count = countryCounts.get(countryId);
			if (min > count.get()) {
				min = count.get();
			}
			if (max < count.get()) {
				max = count.get();
				maxCountry = countryId;
			}
		}

		if (max - min >= 1) {
			return maxCountry;
		} else {
			return 0;
		}
	}

	public int selectRole() {
		int min = Integer.MAX_VALUE;
		int minRole = 0;

		int max = 0;
		for (Integer countryId : roleCounts.keySet()) {
			AtomicInteger count = roleCounts.get(countryId);
			if (min > count.get()) {
				minRole = countryId;
				min = count.get();
			}
			if (max < count.get()) {
				max = count.get();
			}
		}

		if (max - min >= SELECT_PLAYER_COUNT.getValue()) {
			return minRole;
		} else {
			return roleSelector.run();
		}
	}

	public void resetPlayerGameStats(Player player) {
		player.getGameStats().clear();

		// 装备属性
		player.getEquipmentStorage().initEquipmentStat(player);
		player.getHorseEquipmentStorage().initStat(player);
		ItemManager.getInstance().calOnAddSuitStatOnOtherEquipStorage(player, false);

		player.getLifeGridPool().refreshEquipStats(false);
		// 基础等级
		player.getGameStats().addModifiers(
				StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
				playerLevelResource.get(player.getPlayerEnt().getLevel(), true).getRoleStat(
						player.getPlayerEnt().getRole()), false);

		// 背包，仓库开格子的
		player.getPack().initOpenPackHpStat(player, false);
		player.getWareHouse().initOpenPackHpStat(player, false);

		// 坐骑
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.HORSE)) {
			player.getGameStats().addModifiers(Horse.GAME_STATE_ID, player.getHorse().getStat(), false);
			Stat[] playerLevelStat = PlayerManager.getInstance()
					.getPlayerLevelResource(player.getHorse().getCurrentLevel()).getHorseStats();
			player.getGameStats().addModifiers(Horse.PLAYER_LEVEL_STATEID, playerLevelStat, false);
			player.getGameStats().addModifiers(Horse.HORSE_ENHANCE, player.getHorse().getEnhanceStats(), false);
			// player.getGameStats().replaceModifiers(Horse.HORSE_BLESS_STATEID, player.getHorse().getTempBlessStats(), false);
			player.getGameStats().addModifiers(Horse.HORSE_GROWITEM, player.getHorse().getAllGrowItemStats(), false);
			if (player.isRide()) {
				player.getGameStats().addModifiers(Horse.GAME_STATE_SPEEDID, player.getHorse().getSpeedStat(), false);
			}
			for (Integer skillId : player.getHorse().getLearnedSkills().values()) {
				Skill skill = SkillEngine.getInstance().getSkill(null, skillId, player.getObjectId(), 0, 0, player,
						null);
				skill.noEffectorUseSkill();
			}
		} else {
			// 召唤大B哥
			/*
			 * boolean inGreenhandMap =
			 * configValueManager.GREENHAND_MAPS.getValue
			 * ()[player.getCountryValue() - 1] == player .getMapId() ? true :
			 * false;
			 * 
			 * if (inGreenhandMap) {
			 */
			if (player.isRide()) {
				player.getGameStats().addModifiers(Horse.GAME_STATE_SPEEDID, player.getHorse().getSpeedStat(), false);
			}
			// }
		}

		soulService.initSoulStats(player);

		artifactService.initArtifactStats(player);

		// 人品
		// player.getRp().initRpStat();

		// 战魂
		player.getCombatSpiritStorage().initCombatSpiritStorage(player);

		// 技能
		player.getSkillList().usePassiveSkill();
		// VIP
		player.getVip().addStats();

		SoulOfGeneralResource soulRes = soulOfGeneralResources.get(player.getPlayerEnt().getSoulOfGeneral(), true);
		player.getGameStats().addModifiers(SoulOfGeneralResource.GENERAL_SPIRIT, soulRes.getRoleStat(player.getRole()),
				false);
		if (ModuleOpenManager.getInstance().isOpenByKey(player, CountryFlag.COUNTRY_FLAG_MODULE_OPENKEY)) {
			player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
					player.getCountry().getCountryFlag().getResource().getPlayerStats(), false);
		}

		// 时装
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			if (player.getFashionPool().getLevel() > 0) {
				player.getGameStats().addModifiers(FashionPool.FASHION_LEVEL_ID,
						player.getFashionPool().getLevelStats(), false);
			}
		}

		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.WARBOOK)) {
			player.getWarBook().refreshStats(false);
		}

		player.getBeautyGirlPool().refreshForeverStats(false);
		player.getBeautyGirlPool().refreshItemStats(false);

		player.getPromotion().resetPromotionStats(false);

		// 足迹
		player.getFootprintPool().loginInit();

		// 称号
		player.getNicknamePool().refreshModifiers(false);

		// 名将武魂属性
		player.getCollect().getFamedGeneral().initFamedGeneralCollectStats(player);

		player.getSuicide().refreshStats(false);

		// BOSS积分
		player.getBossData().updatesStats(false);

		// 军衔
		player.getMilitary().initMilitaryStat();
		
		player.getSeal().refreshStats(false);

		player.getGameStats().recomputeStats();

		// PS:最后一个addModifiers的recomputeStats参数才设置成true减少计算量
	}

	public synchronized PlayerExpUpdate suicideTurn(Player player) {
		PlayerLevelResource resource = playerLevelResource.get(player.getPlayerEnt().getLevel() + 1, false);
		if (resource == null) {
			return null;
		}

		player.getPlayerEnt().setLevel(resource.getLevel());
		player.getPlayerEnt().setExp(0L);

		PlayerLevelResource res = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
		// // update stats after setting new template
		((PlayerGameStats) player.getGameStats()).doLevelUpgrade(res.getRoleStat(player.getPlayerEnt().getRole()));
		player.getLifeStats().updateCurrentStats();
		player.getLevelLog().levelUp(player.getLevel());
		PacketSendUtility
				.broadcastPacketAndReceiver(player, SM_LEVEL_UPDATE.valueOf(player.getObjectId(), player.getPlayerEnt()
						.getLevel(), player.getLifeStats().getCurrentHp(), player.getLifeStats().getCurrentMp()));
		if (!player.getLifeStats().isAlreadyDead()) {
			player.getLifeStats().fullStoreHpAndMp();
		}

		// 升级事件
		EventBusManager.getInstance().submit(
				LevelUpEvent.valueOf(player.getPlayerEnt().getGuid(), player.getPlayerEnt().getLevel(),
						player.getName()));

		LogManager.levelUpLog(player, System.currentTimeMillis(), player.getSession().getInetIp());

		return PlayerExpUpdate.valueOf(player.getPlayerEnt().getLevel(), resource.getExp(), player.getPlayerEnt()
				.getExp());
	}

	public PlayerExpUpdate levelUp(Player player) {
		PlayerLevelResource resource = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
		long needExp = resource.getExp();
		player.getPlayerEnt().setExp(player.getPlayerEnt().getExp() + needExp);
		// 升级所需经验
		boolean levelUp = false;
		int suicideTurn = player.getSuicide().getTurn();
		while (player.getPlayerEnt().getExp() >= needExp) {
			if (needExp == 0 || suicideTurn < resource.getTurnNum()) {
				// 满级
				// 将多余的经验去除掉
				player.getPlayerEnt().setExp(0);
				break;
			}
			// 升级
			player.getPlayerEnt().setLevel(player.getPlayerEnt().getLevel() + 1);
			player.getPlayerEnt().setExp(player.getPlayerEnt().getExp() - needExp);
			levelUp = true;
			resource = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
			needExp = resource.getExp();
		}
		if (levelUp) {
			PlayerLevelResource res = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
			((PlayerGameStats) player.getGameStats()).doLevelUpgrade(res.getRoleStat(player.getPlayerEnt().getRole()));
			if (!player.getLifeStats().isAlreadyDead()) {
				player.getLifeStats().updateCurrentStats();
			}

			PacketSendUtility.broadcastPacketAndReceiver(player, SM_LEVEL_UPDATE.valueOf(player.getObjectId(), player
					.getPlayerEnt().getLevel(), player.getLifeStats().getMaxHp(), player.getLifeStats().getMaxMp()));
			// 升级事件
			EventBusManager.getInstance().submit(
					LevelUpEvent.valueOf(player.getPlayerEnt().getGuid(), player.getPlayerEnt().getLevel(),
							player.getName()));
		}
		return PlayerExpUpdate.valueOf(player.getPlayerEnt().getLevel(), needExp, player.getPlayerEnt().getExp());
	}

	public synchronized PlayerExpUpdate addExp(Player player, long exp, boolean log, ModuleInfo moduleInfo) {
		if (exp < 0) {
			throw new RuntimeException(String.format("add exp [%s]!", new Object[] { exp }));
		}
		player.getPlayerEnt().setExp(player.getPlayerEnt().getExp() + exp);
		// 升级所需经验
		long needExp = playerLevelResource.get(player.getPlayerEnt().getLevel(), true).getExp();
		PlayerLevelResource resource = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);

		boolean levelUp = false;
		int suicideTurn = player.getSuicide().getTurn();
		while (player.getPlayerEnt().getExp() >= needExp) {
			if (needExp == 0 || suicideTurn < resource.getTurnNum()) {
				// 满级
				// 将多余的经验去除掉
				player.getPlayerEnt().setExp(0);
				break;
			}
			// 升级
			player.getPlayerEnt().setLevel(player.getPlayerEnt().getLevel() + 1);
			player.getPlayerEnt().setExp(player.getPlayerEnt().getExp() - needExp);
			levelUp = true;
			resource = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
			needExp = resource.getExp();
		}
		if (levelUp) {
			PlayerLevelResource res = playerLevelResource.get(player.getPlayerEnt().getLevel(), true);
			// // update stats after setting new template
			((PlayerGameStats) player.getGameStats()).doLevelUpgrade(res.getRoleStat(player.getPlayerEnt().getRole()));
			player.getLifeStats().updateCurrentStats();
			player.getLevelLog().levelUp(player.getLevel());
			PacketSendUtility.broadcastPacketAndReceiver(player, SM_LEVEL_UPDATE.valueOf(player.getObjectId(), player
					.getPlayerEnt().getLevel(), player.getLifeStats().getCurrentHp(), player.getLifeStats()
					.getCurrentMp()));
			if (!player.getLifeStats().isAlreadyDead()) {
				player.getLifeStats().fullStoreHpAndMp();
			}

			// 升级事件
			EventBusManager.getInstance().submit(
					LevelUpEvent.valueOf(player.getPlayerEnt().getGuid(), player.getPlayerEnt().getLevel(),
							player.getName()));

			LogManager.levelUpLog(player, System.currentTimeMillis(), player.getSession().getInetIp());
		}
		WorldMapInstance worldMapInstance = player.getCopyHistory().getCurrentMapInstance();
		if (worldMapInstance != null) {
			worldMapInstance.getCopyInfo().addExp(exp);
		}
		if (log) {
			LogManager.addExp(player, System.currentTimeMillis(), moduleInfo, exp, player.getLevel());
		}
		return PlayerExpUpdate.valueOf(player.getPlayerEnt().getLevel(), exp, player.getPlayerEnt().getExp());
	}

	public Player getPlayerByAccount(String account, String op, String server) {
		if (!this.isIndexExist(account, op, server)) {
			return null;
		}
		PlayerEnt ent = playerDbService.unique("accountIndex", account + "_" + op + "_" + server);
		return ent == null ? null : this.getPlayer(ent.getId());
	}

	public Player getPlayer(long playerId) {
		PlayerEnt playerEnt = playerDbService.load(playerId);
		if (playerEnt != null) {
			// 初始化
			if (playerEnt.getPlayer() == null) {
				playerEnt.creatRealPlayer();
			}

			for (Entry<ModuleKey, ModuleHandle> entry : initUpdateHandles.entrySet()) {
				entry.getValue().deserialize(playerEnt);
			}
		} else {
			return null;
		}
		return playerEnt.getPlayer();
	}

	public Player getTargetPlayer(long playerId) {
		Player player = getPlayer(playerId);
		if (player == null) {
			return null;
		}
		if (player.getGameStats().statSize() == 0) {
			resetPlayerGameStats(player);
			player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
					player.getCountry().getCountryFlag().getResource().getPlayerStats());
		}
		return player;
	}

	/**
	 * 提交持久化任务
	 * 
	 * @param ent
	 */
	public void updatePlayer(Player player) {
		playerDbService.writeBack(player.getPlayerEnt().getId(), player.getPlayerEnt());
		playerStatDbService.writeBack(player.getPlayerStat().getId(), player.getPlayerStat());
	}

	public void updateIfOffline(Player player) {
		if (!SessionManager.getInstance().isOnline(player.getObjectId())) {
			updatePlayer(player);
		}
	}

	public boolean serialize(PlayerEnt ent) {
		for (Entry<ModuleKey, ModuleHandle> entry : initUpdateHandles.entrySet()) {
			// 全局更新
			entry.getValue().serialize(ent);
		}
		return true;
	}

	public PlayerEnt getByName(String playerName) {
		if (!this.isNameExist(playerName)) {
			return null;
		}
		return playerDbService.unique("name", playerName);
	}

	public void registerHandle(ModuleHandle playerInitUpdateHandle) {
		if (!this.initUpdateHandles.containsKey(playerInitUpdateHandle.getModule())) {
			this.initUpdateHandles.put(playerInitUpdateHandle.getModule(), playerInitUpdateHandle);
		} else {
			throw new RuntimeException(String.format("ModuleKey[%s]重复!", playerInitUpdateHandle.getModule()));
		}
	}

	public boolean validate(String account, String op, String server, String sign, String time, int opVip) {
		try {
			String expect = CryptUtils.md5(account + op + server + time + opVip + serverConfigValue.getKey());
			if (expect.equals(sign)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public ConfigValue<Integer> getPK_DURATION_TIME() {
		return PK_DURATION_TIME;
	}

	public void setPK_DURATION_TIME(ConfigValue<Integer> pK_DURATION_TIME) {
		PK_DURATION_TIME = pK_DURATION_TIME;
	}

	public static void loginCopyPosition(int mapId, PlayerEnt playerEnt) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);
		Player player = playerEnt.getPlayer();
		if (worldMap.isCopy()) {
			CopyResource copyResource = (player == null ? null : playerEnt.getPlayer().getCopyHistory()
					.getCurrentCopyResource());
			if (player != null && copyResource != null && copyResource.getType() == CopyType.EXP
					&& player.getPosition() != null && worldMap.getInstances().containsKey(player.getInstanceId())) {
				// 进入经验副本
				WorldPosition position = World.getInstance().createPosition(mapId, player.getInstanceId(),
						player.getX(), player.getY(), player.getHeading());
				playerEnt.getPlayer().setPosition(position);
			} else {
				// 回家
				if (playerEnt.getPlayer() != null && playerEnt.getPlayer().getCopyHistory().getRouteStep() != null) {
					RouteStep loc = playerEnt.getPlayer().getCopyHistory().getRouteStep();
					WorldPosition position = World.getInstance().createPosition(loc.getMapId(), 1, loc.getX(),
							loc.getY(), (byte) 0);
					playerEnt.getPlayer().setPosition(position);
				} else if (playerEnt.getPlayer() != null) {
					// 避免出错，让他回新手村
					List<String> result = ChooserManager.getInstance().chooseValueByRequire(playerEnt.getCountry(),
							ConfigValueManager.getInstance().BIRTH_POINT.getValue());
					RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
					WorldPosition position = World.getInstance().createPosition(p.getMapId(), 1, p.getX(), p.getY(),
							(byte) 0);
					playerEnt.getPlayer().setPosition(position);
				}
			}
		}

		if (playerEnt.getPlayer() != null) {
			MapResource mapResource = World.getInstance().getMapResource(playerEnt.getPlayer().getMapId());
			if (mapResource.getCountry() != playerEnt.getPlayer().getCountryValue()) {
				if (mapResource.getForbidEnterCondition() != null) {
					if (mapResource.getForbidEnterCondition().verify(playerEnt.getPlayer(), false)) {
						List<String> result = ChooserManager.getInstance().chooseValueByRequire(playerEnt.getCountry(),
								PlayerManager.getInstance().BACKHOME_POINT.getValue());
						RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
						World.getInstance().setPosition(playerEnt.getPlayer(), p.getMapId(), p.getX(), p.getY(),
								(byte) 0);
					}
				}
			}
		}

	}

	public CoreConditions getDeadNoticeConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance()
					.getCoreConditions(1, DEAD_NOTICE_CONDITIONID.getValue());
		}
		return coreConditions;
	}

	public void doPlayerPositionFix(Player player) {
		player.getCopyHistory().refresh();
		if (player.getPosition() != null) {
			PlayerManager.loginCopyPosition(player.getMapId(), player.getPlayerEnt());
		} else {
			PlayerManager.loginCopyPosition(player.getPlayerEnt().getMapId(), player.getPlayerEnt());
		}
	}
}
