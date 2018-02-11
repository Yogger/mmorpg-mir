package com.mmorpg.mir.model.serverstate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.blackshop.model.BlackShopServer;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.model.CommonGoldTreasuryServer;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureTotalServers;
import com.mmorpg.mir.model.commonactivity.packet.SM_WeekCri_Count_Change;
import com.mmorpg.mir.model.commonactivity.resource.CommonGoldTreasuryResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.drop.model.MonsterKilledHistory;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.model.ActivityEnum;
import com.mmorpg.mir.model.openactive.model.ActivityInfo;
import com.mmorpg.mir.model.openactive.model.GroupPurchase;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseThree;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseTwo;
import com.mmorpg.mir.model.openactive.model.RechargeCelebrate;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseResource;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseThreeResource;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseTwoResource;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeServer;
import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegeServer;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.config.ServerStateConfig;
import com.mmorpg.mir.model.serverstate.entity.ServerStateEnt;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.Scheduled;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Component
public class ServerState {
	private static final Logger logger = Logger.getLogger(ServerState.class);
	@Inject
	private EntityCacheService<Integer, ServerStateEnt> serverStateEntDbService;

	@Static
	private Storage<String, GroupPurchaseResource> groupPurchaseStorage;

	@Static
	private Storage<String, GroupPurchaseTwoResource> groupPurchaseTwoStorage;

	@Static
	private Storage<String, GroupPurchaseThreeResource> groupPurchaseThreeStorage;

	private String serverName;

	@Static("COMMONACTIVITY:WEEK_CRI_COUNT_ADD_CONDS")
	public ConfigValue<String[]> WEEK_CRI_COUNT_ADD_CONDS;

	@Static("PUBLICTEST:GROUPPURCHASE_INIT_COND")
	private ConfigValue<String> GROUPPURCHASE_INIT_CONDS;

	@Static("OPENACTIVE:GROUPPURCHASE_TWO_INIT_CONDS")
	private ConfigValue<String> GROUPPURCHASE_TWO_INIT_CONDS;

	@Static("OPENACTIVE:GROUPPURCHASE_THREE_INIT_CONDS")
	private ConfigValue<String> GROUPPURCHASE_THREE_INIT_CONDS;

	@Static("OPENACTIVE:MERGE_BLACKSHOP_INIT_GROUPID")
	private ConfigValue<String> MERGE_BLACKSHOP_INIT_GROUPID;

	@Static("BLACKSHOP:MERGE_BLACKSHOP_INIT_BEGIN_TIME")
	private ConfigValue<Integer> MERGE_BLACKSHOP_INIT_BEGIN_TIME;

	@Static("BLACKSHOP:MERGE_BLACKSHOP_INIT_END_TIME")
	private ConfigValue<Integer> MERGE_BLACKSHOP_INIT_END_TIME;

	@Autowired
	private CoreConditionManager conditionManager;

	@Autowired
	private ItemManager itemManager;

	private ServerStateEnt serverEnt;

	public ServerStateEnt getServerEnt() {
		return serverEnt;
	}

	private static ServerState instance;

	private NonBlockingHashMap<String, MonsterKilledHistory> monsterKilledHis;

	private NonBlockingHashMap<String, Integer> serverLimitMap;

	private Map<Integer, AtomicInteger> levelRewardMap;

	private Set<Long> levelRewardLog;

	private NonBlockingHashSet<Long> attendRiotLog;

	private AtomicBoolean flagOpenServer;

	private AtomicBoolean diplomacyOpenServer;
	/** 奇虎360大厅特权 */
	private QiHu360PrivilegeServer qiHu360PrivilegeServer;

	private QiHu360SpeedPrivilegeServer qiHu360SpeedPrivilegeServer;

	// 已经参团的人 key:GroupPurchaseResource#id
	private NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers;

	// 玩家的团购数据
	private ConcurrentHashMap<Long, GroupPurchase> playerGroupPurchases;

	// 超值团购2 已经参团的人 key:GroupPurchaseResource#id
	private NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers2;

	// 超值团购2 玩家的团购数据
	private ConcurrentHashMap<Long, GroupPurchaseTwo> playerGroupPurchases2;

	// 超值团购3 已经参团的人 key:GroupPurchaseResource#id
	private NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers3;

	// 超值团购3玩家的团购数据
	private ConcurrentHashMap<Long, GroupPurchaseThree> playerGroupPurchases3;

	@Static("SERVERSTATE:QIHU360_SPEED_PRIVILEGE_SERVER_TIME")
	private ConfigValue<String[]> QIHU360_SPEED_PRIVILEGE_SERVER_TIME;

	@Static("SERVERSTATE:QIHU360_PRIVILEGE_SERVER_TIME2")
	private ConfigValue<String[]> QIHU360_PRIVILEGE_SERVER_TIME2;

	private NonBlockingHashMap<Integer, ActivityInfo> celebrateActivityInfos;

	private NonBlockingHashMap<Long, RechargeCelebrate> celebrateRecharge;

	/** 黑市全局信息 */
	private BlackShopServer blackShopServer;

	/** 鉴宝活动 */
	private CommonIdentifyTreasureTotalServers commonIdentifyTreasureTotalServers;

	@Static("SERVERSTATE:IDENTIFY_TREASURE_OPEN_TIME")
	public ConfigValue<Map<String, ArrayList<String>>> IDENTIFY_TREASURE_OPEN_TIME;

	private Map<String, CommonGoldTreasuryServer> goldTreasuryServers;

	@Autowired
	private CountryManager countryManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		serverEnt = serverStateEntDbService.loadOrCreate(1, new EntityBuilder<Integer, ServerStateEnt>() {
			@Override
			public ServerStateEnt newInstance(Integer id) {
				ServerStateEnt ent = new ServerStateEnt();
				ent.setId(id);
				return ent;
			}
		});
		instance = this;
		if (serverEnt.getMonsterKillHisJson() == null) {
			monsterKilledHis = new NonBlockingHashMap<String, MonsterKilledHistory>();
		} else {
			monsterKilledHis = new NonBlockingHashMap<String, MonsterKilledHistory>();
			Map<String, MonsterKilledHistory> hashMap = JsonUtils.string2Map(serverEnt.getMonsterKillHisJson(),
					String.class, MonsterKilledHistory.class);
			for (Entry<String, MonsterKilledHistory> entry : hashMap.entrySet()) {
				monsterKilledHis.put(entry.getKey(), entry.getValue());
			}
		}
		if (serverEnt.getLastKillMonterDate() != null && DateUtils.isToday(serverEnt.getLastKillMonterDate())) {
			for (MonsterKilledHistory hist : monsterKilledHis.values()) {
				hist.refreshDropHistory(itemManager);
			}
			serverEnt.setLastKillMonterDate(new Date());
		}

		if (serverEnt.getServerLimitJson() == null) {
			serverLimitMap = new NonBlockingHashMap<String, Integer>();
		} else {
			serverLimitMap = new NonBlockingHashMap<String, Integer>();
			Map<String, Integer> map = JsonUtils
					.string2Map(serverEnt.getServerLimitJson(), String.class, Integer.class);
			for (Entry<String, Integer> entry : map.entrySet()) {
				serverLimitMap.put(entry.getKey(), entry.getValue());
			}
		}

		if (serverEnt.getLevelRewardJson() == null) {
			levelRewardMap = New.hashMap();
			for (CountryId id : CountryId.values()) {
				levelRewardMap.put(id.getValue(), new AtomicInteger());
			}
		} else {
			levelRewardMap = New.hashMap();
			Map<Integer, Integer> map = JsonUtils.string2Map(serverEnt.getLevelRewardJson(), Integer.class,
					Integer.class);
			for (CountryId id : CountryId.values()) {
				Integer count = map.get(id.getValue());
				if (count == null) {
					levelRewardMap.put(id.getValue(), new AtomicInteger());
				} else {
					levelRewardMap.put(id.getValue(), new AtomicInteger(count));
				}
			}
		}

		levelRewardLog = new NonBlockingHashSet<Long>();
		if (serverEnt.getLevelRewardLogJson() != null) {
			Long[] list = JsonUtils.string2Array(serverEnt.getLevelRewardLogJson(), Long.class);
			for (Long pid : list) {
				levelRewardLog.add(pid);
			}
		}
		attendRiotLog = new NonBlockingHashSet<Long>();
		if (serverEnt.getAttendRiotLogJson() != null) {
			Long[] list = JsonUtils.string2Array(serverEnt.getAttendRiotLogJson(), Long.class);
			for (Long pid : list) {
				attendRiotLog.add(pid);
			}
		}
		flagOpenServer = new AtomicBoolean();
		diplomacyOpenServer = new AtomicBoolean();
		Date openDate = serverEnt.getOpenServerDate();
		if (openDate != null && System.currentTimeMillis() >= countryManager.getOpenServerFlagSpawnTime(openDate)) {
			flagOpenServer.set(true);
		}
		if (openDate != null && System.currentTimeMillis() >= countryManager.getOpenServerDiplomacySpawnTime(openDate)) {
			diplomacyOpenServer.set(true);
		}

		boolean initVerify = conditionManager.getCoreConditions(1, GROUPPURCHASE_INIT_CONDS.getValue()).verify(null);
		if (serverEnt.getGroupPurchasePlayersJson() == null) {
			if (initVerify) {
				groupPurchasePlayers = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
				for (GroupPurchaseResource resource : groupPurchaseStorage.getAll()) {
					groupPurchasePlayers.put(resource.getId(), new NonBlockingHashSet<Long>());
				}

			}
		} else {
			groupPurchasePlayers = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
			Map<String, NonBlockingHashSet> map = JsonUtils.string2Map(serverEnt.getGroupPurchasePlayersJson(),
					String.class, NonBlockingHashSet.class);

			for (Entry<String, NonBlockingHashSet> entry : map.entrySet()) {
				groupPurchasePlayers.put(entry.getKey(), entry.getValue());
			}
		}

		if (serverEnt.getPlayerGroupPurchaseJson() == null) {
			if (initVerify) {
				playerGroupPurchases = new ConcurrentHashMap<Long, GroupPurchase>();
			}
		} else {
			Map<Long, GroupPurchase> map = JsonUtils.string2Map(serverEnt.getPlayerGroupPurchaseJson(), Long.class,
					GroupPurchase.class);
			playerGroupPurchases = new ConcurrentHashMap<Long, GroupPurchase>(map);
		}

		boolean initVerifyTwo = conditionManager.getCoreConditions(1, GROUPPURCHASE_TWO_INIT_CONDS.getValue()).verify(
				null);
		if (null == serverEnt.getGroupPurchasePlayersJson2()) {
			if (initVerifyTwo) {
				groupPurchasePlayers2 = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
				for (GroupPurchaseTwoResource resource : groupPurchaseTwoStorage.getAll()) {
					groupPurchasePlayers2.put(resource.getId(), new NonBlockingHashSet<Long>());
				}
			}
		} else {
			groupPurchasePlayers2 = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
			Map<String, NonBlockingHashSet> map = JsonUtils.string2Map(serverEnt.getGroupPurchasePlayersJson2(),
					String.class, NonBlockingHashSet.class);

			for (Entry<String, NonBlockingHashSet> entry : map.entrySet()) {
				groupPurchasePlayers2.put(entry.getKey(), entry.getValue());
			}
		}

		if (null == serverEnt.getPlayerGroupPurchaseJson2()) {
			if (initVerifyTwo) {
				playerGroupPurchases2 = new ConcurrentHashMap<Long, GroupPurchaseTwo>();
			}
		} else {
			Map<Long, GroupPurchaseTwo> map = JsonUtils.string2Map(serverEnt.getPlayerGroupPurchaseJson2(), Long.class,
					GroupPurchaseTwo.class);
			playerGroupPurchases2 = new ConcurrentHashMap<Long, GroupPurchaseTwo>(map);
		}

		boolean initVerifyThree = conditionManager.getCoreConditions(1, GROUPPURCHASE_THREE_INIT_CONDS.getValue())
				.verify(null);
		if (null == serverEnt.getGroupPurchasePlayersJson3()) {
			if (initVerifyThree) {
				groupPurchasePlayers3 = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
				for (GroupPurchaseThreeResource resource : groupPurchaseThreeStorage.getAll()) {
					groupPurchasePlayers3.put(resource.getId(), new NonBlockingHashSet<Long>());
				}
			}
		} else {
			groupPurchasePlayers3 = new NonBlockingHashMap<String, NonBlockingHashSet<Long>>();
			Map<String, NonBlockingHashSet> map = JsonUtils.string2Map(serverEnt.getGroupPurchasePlayersJson3(),
					String.class, NonBlockingHashSet.class);

			for (Entry<String, NonBlockingHashSet> entry : map.entrySet()) {
				groupPurchasePlayers3.put(entry.getKey(), entry.getValue());
			}
		}

		if (null == serverEnt.getPlayerGroupPurchaseJson3()) {
			if (initVerifyThree) {
				playerGroupPurchases3 = new ConcurrentHashMap<Long, GroupPurchaseThree>();
			}
		} else {
			Map<Long, GroupPurchaseThree> map = JsonUtils.string2Map(serverEnt.getPlayerGroupPurchaseJson3(),
					Long.class, GroupPurchaseThree.class);
			playerGroupPurchases3 = new ConcurrentHashMap<Long, GroupPurchaseThree>(map);
		}

		if (serverEnt.getQiHu360PrivilegeServerJson() == null) {
			qiHu360PrivilegeServer = new QiHu360PrivilegeServer();
			String[] serverTime = QIHU360_PRIVILEGE_SERVER_TIME2.getValue();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = null;
			Date entTime = null;
			try {
				startTime = format.parse(serverTime[0]);
				entTime = format.parse(serverTime[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			qiHu360PrivilegeServer.setstartTime(startTime);
			qiHu360PrivilegeServer.setEndTime(entTime);
			qiHu360PrivilegeServer.setVersion(1);
		} else {
			qiHu360PrivilegeServer = JsonUtils.string2Object(serverEnt.getQiHu360PrivilegeServerJson(),
					QiHu360PrivilegeServer.class);
		}
		if (serverEnt.getQiHu360SpeedPrivilegeServerJson() == null) {
			qiHu360SpeedPrivilegeServer = new QiHu360SpeedPrivilegeServer();
			String[] serverTime = QIHU360_SPEED_PRIVILEGE_SERVER_TIME.getValue();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = null;
			Date entTime = null;
			try {
				startTime = format.parse(serverTime[0]);
				entTime = format.parse(serverTime[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			qiHu360SpeedPrivilegeServer.setStartTime(startTime);
			qiHu360SpeedPrivilegeServer.setEndTime(entTime);
			qiHu360SpeedPrivilegeServer.setVersion(1);
		} else {
			qiHu360SpeedPrivilegeServer = JsonUtils.string2Object(serverEnt.getQiHu360SpeedPrivilegeServerJson(),
					QiHu360SpeedPrivilegeServer.class);
		}
		celebrateActivityInfos = new NonBlockingHashMap<Integer, ActivityInfo>();
		if (serverEnt.getCelebrateActivityInfoJson() != null) {
			celebrateActivityInfos = new NonBlockingHashMap<Integer, ActivityInfo>();
			Map<Integer, ActivityInfo> map = JsonUtils.string2Map(serverEnt.getCelebrateActivityInfoJson(),
					Integer.class, ActivityInfo.class);
			celebrateActivityInfos.putAll(map);
		} else {
			Date startTime = DateUtils.string2Date("2015-11-30 00:00:00", DateUtils.PATTERN_DATE_TIME);
			Date endTime = DateUtils.string2Date("2015-11-30 23:59:59", DateUtils.PATTERN_DATE_TIME);
			for (ActivityEnum type : ActivityEnum.values()) {
				celebrateActivityInfos.put(type.getValue(),
						ActivityInfo.valueOf(startTime.getTime(), endTime.getTime()));
			}
		}

		celebrateRecharge = new NonBlockingHashMap<Long, RechargeCelebrate>();
		if (serverEnt.getRechargeCelebrateJson() != null) {
			Map<Long, RechargeCelebrate> map = JsonUtils.string2Map(serverEnt.getRechargeCelebrateJson(), Long.class,
					RechargeCelebrate.class);
			celebrateRecharge.putAll(map);
		}

		if (serverEnt.getBlackShopServerJson() != null) {
			blackShopServer = JsonUtils.string2Object(serverEnt.getBlackShopServerJson(), BlackShopServer.class);
		} else {
			blackShopServer = BlackShopServer.valueOf();
		}
		blackShopServer.doMerge(MERGE_BLACKSHOP_INIT_GROUPID.getValue(), serverEnt.getLastMergeTime(),
				MERGE_BLACKSHOP_INIT_BEGIN_TIME.getValue(), MERGE_BLACKSHOP_INIT_END_TIME.getValue());

		if (serverEnt.getCommonIdentifyTreasureTotalServersJson() != null) {
			commonIdentifyTreasureTotalServers = JsonUtils.string2Object(
					serverEnt.getCommonIdentifyTreasureTotalServersJson(), CommonIdentifyTreasureTotalServers.class);
		} else {
			commonIdentifyTreasureTotalServers = CommonIdentifyTreasureTotalServers.valueOf();
		}

		if (serverEnt.getWeekCriOpenCount() == null) {
			serverEnt.setWeekCriOpenCount(-1);
		}

		Date weekCriTime = serverEnt.getWeekCriLastCheckTime();
		CoreConditions conds = conditionManager.getCoreConditions(1, WEEK_CRI_COUNT_ADD_CONDS.getValue());
		if (weekCriTime == null) {
			Integer openCount = -1;
			if (conds.verify(null)) {
				openCount++;
			}
			serverEnt.setWeekCriOpenCount(openCount);
		} else {
			if (conds.verify(null) && !DateUtils.isToday(weekCriTime)) {
				int openCount = serverEnt.getWeekCriOpenCount();
				openCount++;
				serverEnt.setWeekCriOpenCount(openCount);
				serverEnt.setWeekCriLastCheckTime(new Date());
			}
		}

		Properties pros = new Properties();
		InputStream in = null;
		try {
			in = new ClassPathResource("server.properties").getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			pros.load(reader);
			if (pros.containsKey("server.config.servername")) {
				this.serverName = pros.getProperty("server.config.servername");
			} else {
				this.serverName = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (serverEnt.getGoldTreasuryServersJson() != null) {
			goldTreasuryServers = JsonUtils.string2Map(serverEnt.getGoldTreasuryServersJson(), String.class,
					CommonGoldTreasuryServer.class);
		} else {
			goldTreasuryServers = new HashMap<String, CommonGoldTreasuryServer>();
		}
	}

	public void initAll() {
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, ArrayList<String>> openTimes = IDENTIFY_TREASURE_OPEN_TIME.getValue();
		Map<String, CommonIdentifyTreasureServer> treasuerServers = commonIdentifyTreasureTotalServers
				.getTreasureServers();
		for (String activeName : openTimes.keySet()) {
			if (treasuerServers.containsKey(activeName))
				continue;
			long startTime = 0;
			long endTime = 0;
			try {
				startTime = format.parse(openTimes.get(activeName).get(0)).getTime();
				endTime = format.parse(openTimes.get(activeName).get(1)).getTime();
			} catch (Exception e) {
				new RuntimeException("解析鉴宝活动自动开启时间错误");
			}
			CommonIdentifyTreasureServer treasureSever = CommonIdentifyTreasureServer.valueOf(activeName);
			treasuerServers.put(activeName, treasureSever);
			treasureSever.refresh(activeName, startTime, endTime);
		}
		for (CommonGoldTreasuryResource resource : CommonActivityConfig.getInstance().goldTreasuryStorage.getAll()) {
			if (!goldTreasuryServers.containsKey(resource.getActiveName())) {
				goldTreasuryServers.put(resource.getActiveName(), CommonGoldTreasuryServer.valueOf());
			}
		}
	}

	public boolean neverStartFlag() {
		return flagOpenServer.compareAndSet(false, true);
	}

	public boolean getNeverStartFlag() {
		return flagOpenServer.get();
	}

	public boolean neverStartDiplomacy() {
		return diplomacyOpenServer.compareAndSet(false, true);
	}

	public boolean getNeverStartDiplomacy() {
		return diplomacyOpenServer.get();
	}

	public boolean huntMonsterCountVerify(String objectKey, Operator op, int value) {
		int count = monsterKilledHis.containsKey(objectKey) ? monsterKilledHis.get(objectKey).getKilledCount() : 0;
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		return false;
	}

	public boolean huntMonstersCountVerify(String[] objectKeys, Operator op, int value) {
		int count = 0;
		for (int i = 0; i < objectKeys.length; i++) {
			MonsterKilledHistory monsterCountHis = monsterKilledHis.get(objectKeys[i]);
			if (monsterCountHis != null) {
				count += monsterCountHis.getKilledCount();
			}
		}
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		return false;
	}

	public boolean monsterDropCountVerify(String monterKey, String itemKey, Operator op, int value) {
		int count = monsterKilledHis.containsKey(monterKey) ? monsterKilledHis.get(monterKey).getItemCount(itemKey) : 0;
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		return false;
	}

	public boolean monstersDropCountVerify(String[] objKeys, String itemId, Operator op, int value) {
		int count = 0;
		for (int i = 0; i < objKeys.length; i++) {
			MonsterKilledHistory monsterCountHis = monsterKilledHis.get(objKeys[i]);
			if (monsterCountHis != null) {
				count += monsterCountHis.getItemCount(itemId);
			}
		}
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		return false;
	}

	public boolean itemDropCountVerify(String itemKey, Operator op, int value) {
		int count = 0;
		for (MonsterKilledHistory mkh : monsterKilledHis.values()) {
			count += mkh.getItemCount(itemKey);
		}
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		return false;
	}

	public boolean serverLimitVerify(String limitKey) {
		Integer count = serverLimitMap.get(limitKey);
		if (count != null) {
			Integer configCount = ServerStateConfig.getInstance().SERVER_LIMIT_MAP.getValue().get(limitKey);
			int config = configCount == null ? 0 : configCount;
			if (count >= config) {
				return false;
			}
		}
		return true;
	}

	public Map<String, Integer> getServerLimitStatus() {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		for (Entry<String, Integer> entry : ServerStateConfig.getInstance().SERVER_LIMIT_MAP.getValue().entrySet()) {
			Integer count = serverLimitMap.get(entry.getKey());
			String key = OpenActiveConfig.getInstance().SERVER_RESOURCE_ID.getValue().get(entry.getKey());
			ret.put(key, entry.getValue() - (count == null ? 0 : count));
		}
		return ret;
	}

	@Scheduled(name = "系统0点清理掉落记录", value = "0 0 0 * * *")
	public void anotherDay() {
		if (ClearAndMigrate.clear) {
			return;
		}
		for (MonsterKilledHistory his : monsterKilledHis.values()) {
			his.refreshDropHistory(itemManager);
		}
		serverEnt.setLastKillMonterDate(new Date());

		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void update() {
		if (monsterKilledHis != null) {
			serverEnt.setMonsterKillHisJson(JsonUtils.object2String(monsterKilledHis));
		}
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	@Scheduled(name = "存储杀怪掉落记录", value = "0 */5 * * * *")
	public void saveMonsterKill() {
		if (ClearAndMigrate.clear) {
			return;
		}
		if (monsterKilledHis != null) {
			serverEnt.setMonsterKillHisJson(JsonUtils.object2String(monsterKilledHis));
		}
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void addKill(String key) {
		if (!monsterKilledHis.containsKey(key)) {
			monsterKilledHis.put(key, MonsterKilledHistory.valueOf(key));
		}
		monsterKilledHis.get(key).addKilledCount();
	}

	public void addLimit(String key) {
		if (!serverLimitMap.containsKey(key)) {
			serverLimitMap.put(key, 1);
		} else {
			serverLimitMap.put(key, serverLimitMap.get(key) + 1);
		}
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public Date getOpenServerDate() {
		return serverEnt.getOpenServerDate();
	}

	public Date getLastKingOfWarDate() {
		return serverEnt.getLastOpenKingOfWarDate();
	}

	public Date getLastGangOfWarDate() {
		return serverEnt.getLastOpenGangOfWarDate();
	}

	public Date getLastMonsterRiotEndTime(int countryValue) {
		if (countryValue == CountryId.C1.getValue()) {
			return serverEnt.getLastQiMonsterRiotDate();
		} else if (countryValue == CountryId.C2.getValue()) {
			return serverEnt.getLastChuMonsterRiotDate();
		}
		return serverEnt.getLastZhaoMonsterRiotDate();
	}

	public boolean isOpenServer() {
		return serverEnt.getOpenServerDate() == null ? false : true;
	}

	public boolean isTodayOpenServer() {
		return isOpenServer() && DateUtils.isToday(getOpenServerDate());
	}

	public void setOpenServerDate(Date date) {
		serverEnt.setOpenServerDate(date);
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void setLastMergeDate(Date date) {
		serverEnt.setLastMergeTime(date);
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void setKingOfWarDate(Date date) {
		serverEnt.setLastOpenKingOfWarDate(date);
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void setGangOfWarDate(Date date) {
		serverEnt.setLastOpenGangOfWarDate(date);
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void setMonsterRiotEndDate(int countryValue, Date date) {
		if (countryValue == CountryId.C1.getValue()) {
			serverEnt.setLastQiMonsterRiotDate(date);
		} else if (countryValue == CountryId.C2.getValue()) {
			serverEnt.setLastChuMonsterRiotDate(date);
		} else {
			serverEnt.setLastZhaoMonsterRiotDate(date);
		}
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void logAttendMonsterRiot(Collection<Player> attenders) {
		for (Player player : attenders) {
			getAttendRiotLog().add(player.getObjectId());
		}
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public static ServerState getInstance() {
		return instance;
	}

	public NonBlockingHashMap<String, MonsterKilledHistory> getMonsterKilledHis() {
		return monsterKilledHis;
	}

	public void setMonsterKilledHis(NonBlockingHashMap<String, MonsterKilledHistory> monsterKilledHis) {
		this.monsterKilledHis = monsterKilledHis;
	}

	public NonBlockingHashMap<String, Integer> getServerLimitMap() {
		return serverLimitMap;
	}

	public void setServerLimitMap(NonBlockingHashMap<String, Integer> serverLimitMap) {
		this.serverLimitMap = serverLimitMap;
	}

	@JsonIgnore
	public Map<String, Integer> getAvailibleMap(String[] keys) {
		Map<String, Integer> map = New.hashMap();
		for (String key : keys) {
			Integer configCount = ServerStateConfig.getInstance().SERVER_LIMIT_MAP.getValue().get(key);
			Integer useCount = serverLimitMap.get(key);
			int availiable = configCount - (useCount == null ? 0 : useCount);
			map.put(key, availiable);
		}
		return map;
	}

	@JsonIgnore
	public boolean hasMerged() {
		return getServerEnt().getLastMergeTime() != null;
	}

	@JsonIgnore
	public Date getMergeTime() {
		return getServerEnt().getLastMergeTime();
	}

	@JsonIgnore
	public int hasMergedDays() {
		if (getMergeTime() == null) {
			return -1;
		}
		return DateUtils.calcIntervalDays(getMergeTime(), new Date());
	}

	public Map<Integer, AtomicInteger> getLevelRewardMap() {
		return levelRewardMap;
	}

	public void setLevelRewardMap(Map<Integer, AtomicInteger> levelRewardMap) {
		this.levelRewardMap = levelRewardMap;
	}

	public void doLevelReward(LevelUpEvent event) {
		if (event.getLevel() >= ServerStateConfig.getInstance().LEVEL_REWARD_BASE.getValue()
				&& !levelRewardLog.contains(event.getOwner())) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			int count = getLevelRewardMap().get(player.getCountryValue()).incrementAndGet();
			int maxCont = ServerStateConfig.getInstance().LEVEL_REWARD_ID.getValue().length;
			if (count > 0 && count <= maxCont) {
				String rewardId = ServerStateConfig.getInstance().LEVEL_REWARD_ID.getValue()[count - 1];
				Reward reward = RewardManager.getInstance().creatReward(player, rewardId, null);
				String title = ServerStateConfig.getInstance().LEVEL_REWARD_MAIL_TITLE.getValue()[count - 1];
				String content = ServerStateConfig.getInstance().LEVEL_REWARD_MAIL_CONTENT.getValue()[count - 1];
				Mail mail = Mail
						.valueOf(
								I18nUtils.valueOf(title),
								I18nUtils.valueOf(content).addParm(I18NparamKey.PLAYERNAME,
										I18nPack.valueOf(player.getName())), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				levelRewardLog.add(player.getObjectId());
			}
			serverStateEntDbService.writeBack(1, serverEnt);
		}
	}

	public Set<Long> getLevelRewardLog() {
		return levelRewardLog;
	}

	public void setLevelRewardLog(Set<Long> levelRewardLog) {
		this.levelRewardLog = levelRewardLog;
	}

	public NonBlockingHashSet<Long> getAttendRiotLog() {
		return attendRiotLog;
	}

	public void setAttendRiotLog(NonBlockingHashSet<Long> attendRiotLog) {
		this.attendRiotLog = attendRiotLog;
	}

	public int getFlagSpecifiedStatus() {
		return serverEnt.getCountryPowerFlagQuest();
	}

	public void openServerCalcCountryPower() {
		serverEnt.setCountryPowerFlagQuest(FlagSpecifiedSatatus.CALCULATE.getValue());
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public void firstFlagQuestAfterCountryPowerCalc() {
		serverEnt.setCountryPowerFlagQuest(FlagSpecifiedSatatus.INIT_FIRSTFLAG.getValue());
		serverStateEntDbService.writeBack(1, serverEnt);
	}

	public NonBlockingHashMap<String, NonBlockingHashSet<Long>> getGroupPurchasePlayers() {
		return groupPurchasePlayers;
	}

	public void setGroupPurchasePlayers(NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers) {
		this.groupPurchasePlayers = groupPurchasePlayers;
	}

	public ConcurrentHashMap<Long, GroupPurchase> getPlayerGroupPurchases() {
		return playerGroupPurchases;
	}

	public void setPlayerGroupPurchases(ConcurrentHashMap<Long, GroupPurchase> playerGroupPurchases) {
		this.playerGroupPurchases = playerGroupPurchases;
	}

	public static void main(String[] args) {
		NonBlockingHashSet<Long> ss = new NonBlockingHashSet<Long>();
		ss.add(123213L);
		ss.add(12321333L);
		String s = JsonUtils.object2String(ss);
		Long[] ll = JsonUtils.string2Array(s, Long.class);
		for (Long l : ll) {
			System.out.println(l);
		}
	}

	public QiHu360PrivilegeServer getQiHu360PrivilegeServer() {
		return qiHu360PrivilegeServer;
	}

	public void setQiHu360PrivilegeServer(QiHu360PrivilegeServer qiHu360PrivilegeServer) {
		this.qiHu360PrivilegeServer = qiHu360PrivilegeServer;
	}

	public NonBlockingHashMap<String, NonBlockingHashSet<Long>> getGroupPurchasePlayers2() {
		return groupPurchasePlayers2;
	}

	public void setGroupPurchasePlayers2(NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers2) {
		this.groupPurchasePlayers2 = groupPurchasePlayers2;
	}

	public ConcurrentHashMap<Long, GroupPurchaseTwo> getPlayerGroupPurchases2() {
		return playerGroupPurchases2;
	}

	public void setPlayerGroupPurchases2(ConcurrentHashMap<Long, GroupPurchaseTwo> playerGroupPurchases2) {
		this.playerGroupPurchases2 = playerGroupPurchases2;
	}

	public NonBlockingHashMap<String, NonBlockingHashSet<Long>> getGroupPurchasePlayers3() {
		return groupPurchasePlayers3;
	}

	public void setGroupPurchasePlayers3(NonBlockingHashMap<String, NonBlockingHashSet<Long>> groupPurchasePlayers3) {
		this.groupPurchasePlayers3 = groupPurchasePlayers3;
	}

	public ConcurrentHashMap<Long, GroupPurchaseThree> getPlayerGroupPurchases3() {
		return playerGroupPurchases3;
	}

	public void setPlayerGroupPurchases3(ConcurrentHashMap<Long, GroupPurchaseThree> playerGroupPurchases3) {
		this.playerGroupPurchases3 = playerGroupPurchases3;
	}

	public NonBlockingHashMap<Integer, ActivityInfo> getCelebrateActivityInfos() {
		return celebrateActivityInfos;
	}

	public void setCelebrateActivityInfos(NonBlockingHashMap<Integer, ActivityInfo> celebrateActivityInfos) {
		this.celebrateActivityInfos = celebrateActivityInfos;
	}

	public void addCelebrateRecharegeConsume(Player player, long gold) {
		if (!celebrateRecharge.containsKey(player.getObjectId())) {
			RechargeCelebrate recharge = RechargeCelebrate.valueOf();
			recharge.addRechargeAmount(player, gold);
			celebrateRecharge.put(player.getObjectId(), recharge);
		} else {
			celebrateRecharge.get(player.getObjectId()).addRechargeAmount(player, gold);
		}
	}

	public boolean isCelebrateActivityOpen(ActivityEnum activityType) {
		ActivityInfo info = celebrateActivityInfos.get(activityType.getValue());
		return info != null && info.isOpenning();
	}

	public void checkAndAddWeekCriCount() {
		Date weekCriLastTime = serverEnt.getWeekCriLastCheckTime();
		if (CommonActivityConfig.getInstance().getWeekCriCountAddCond().verify(null)
				&& !DateUtils.isToday(weekCriLastTime)) {
			int openCount = serverEnt.getWeekCriOpenCount();
			openCount++;
			serverEnt.setWeekCriOpenCount(openCount);
			serverEnt.setWeekCriLastCheckTime(new Date());
			serverStateEntDbService.writeBack(1, serverEnt);
			for (Long pid : SessionManager.getInstance().getOnlineIdentities()) {
				Player player = PlayerManager.getInstance().getPlayer(pid);
				PacketSendUtility.sendPacket(player, SM_WeekCri_Count_Change.valueOf(openCount));
			}
		}
	}

	// public String getServerNamePreffix() {
	// if (StringUtils.isBlank(this.serverName)) {
	// return "";
	// }
	// int index = StringUtils.indexOf(this.serverName, "_");
	// if (index == -1) {
	// return this.serverName;
	// }
	// return StringUtils.substring(this.serverName, 0, index);
	// }

	public NonBlockingHashMap<Long, RechargeCelebrate> getCelebrateRecharge() {
		return celebrateRecharge;
	}

	public void setCelebrateRecharge(NonBlockingHashMap<Long, RechargeCelebrate> celebrateRecharge) {
		this.celebrateRecharge = celebrateRecharge;
	}

	public QiHu360SpeedPrivilegeServer getQiHu360SpeedPrivilegeServer() {
		return qiHu360SpeedPrivilegeServer;
	}

	public void setQiHu360SpeedPrivilegeServer(QiHu360SpeedPrivilegeServer qiHu360SpeedPrivilegeServer) {
		this.qiHu360SpeedPrivilegeServer = qiHu360SpeedPrivilegeServer;
	}

	public BlackShopServer getBlackShopServer() {
		return blackShopServer;
	}

	public void setBlackShopServer(BlackShopServer blackShopServer) {
		this.blackShopServer = blackShopServer;
	}

	public CommonIdentifyTreasureTotalServers getCommonIdentifyTreasureTotalServers() {
		return commonIdentifyTreasureTotalServers;
	}

	public void setCommonIdentifyTreasureTotalServers(
			CommonIdentifyTreasureTotalServers commonIdentifyTreasureTotalServers) {
		this.commonIdentifyTreasureTotalServers = commonIdentifyTreasureTotalServers;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Map<String, CommonGoldTreasuryServer> getGoldTreasuryServers() {
		return goldTreasuryServers;
	}

	public void setGoldTreasuryServers(Map<String, CommonGoldTreasuryServer> goldTreasuryServers) {
		this.goldTreasuryServers = goldTreasuryServers;
	}

	public void upgradeAssassinNpcLevel() {
		serverEnt.setAssassinLevel(serverEnt.getAssassinLevel() + 1);
		serverStateEntDbService.writeBack(1, serverEnt);
	}
	
	@JsonIgnore
	public int getAssassinNpcLevel() {
		return serverEnt.getAssassinLevel();
	}
	
	public void upgradeMinisterNpcLevel() {
		serverEnt.setMinisterLevel(serverEnt.getMinisterLevel() + 1);
		serverStateEntDbService.writeBack(1, serverEnt);
	}
	
	@JsonIgnore
	public int getMinisterNpcLevel() {
		return serverEnt.getMinisterLevel();
	}
	
}
