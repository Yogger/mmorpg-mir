package com.mmorpg.mir.model.rank.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.show.object.HeroMailShow;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.entity.CountryRankEnt;
import com.mmorpg.mir.model.rank.entity.OpenServerCountryRankEnt;
import com.mmorpg.mir.model.rank.entity.WorldRankEnt;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.rank.model.rank.CountryRank;
import com.mmorpg.mir.model.rank.model.rank.OpenServerCountryRank;
import com.mmorpg.mir.model.rank.model.rank.WorldRank;
import com.mmorpg.mir.model.rank.model.rankelement.CountryRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.HeroRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.MilitaryRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.PlayerLevelRankElement;
import com.mmorpg.mir.model.rank.packet.SM_Country_Power_Player;
import com.mmorpg.mir.model.rank.packet.SM_Get_Hero_Reward;
import com.mmorpg.mir.model.rank.packet.SM_World_Level;
import com.mmorpg.mir.model.rank.packet.SM_World_Military_Level;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.FlagSpecifiedSatatus;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.event.event.IEvent;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;

/**
 * @author 37wan
 * 
 */
@Component
public class WorldRankManager implements IWorldRankManager {

	@Inject
	private EntityCacheService<String, WorldRankEnt> rankEntDbService;
	@Inject
	private EntityCacheService<Integer, CountryRankEnt> countryRankEntDBService;
	@Inject
	private EntityCacheService<Integer, OpenServerCountryRankEnt> openServerEntDBService;
	
	private ConcurrentHashMap<RankType, WorldRank> rankMap = new ConcurrentHashMap<RankType, WorldRank>();

	private ConcurrentHashMap<Integer, CountryRank> countryRankMap = new ConcurrentHashMap<Integer, CountryRank>();

	private ConcurrentHashMap<Integer, OpenServerCountryRank> openServerRankMap = new ConcurrentHashMap<Integer, OpenServerCountryRank>();

	private static WorldRankManager INSTANCE;

	@Static("RANK:REQUIREDLEVEL")
	public ConfigValue<Integer> requiredLevel;

	@Static("RANK:MAX_SIZE_MAP")
	public ConfigValue<Map<String, Integer>> MAX_SIZE_MAP;

	@Static("RANK:HERO_REWARD_MAP")
	public ConfigValue<Map<Integer, String>> HERO_REWARD_MAP;

	@Static("RANK:NUMS_PER_PAGE")
	public ConfigValue<Integer> NUMS_PER_PAGE;

	@Static("WORLDLEVEL:CONSTANT")
	public ConfigValue<Integer> WORLD_LEVEL_CONSTANT;

	@Static("RANK:YESTERDAY_HERO_MAIL_TITLE")
	public ConfigValue<String> YESTERDAY_HERO_MAIL_TITLE;

	@Static("RANK:YESTERDAY_HERO_MAIL_CONTENT")
	public ConfigValue<String> YESTERDAY_HERO_MAIL_CONTENT;

	@Static("PUBLIC:WORLD_LEVEL_VALID_BASE")
	public ConfigValue<Integer> WORLD_LEVEL_VALID_BASE;

	@Static("RANK:REFRESH_COUNTRY_POWER_AFTER_OPEN")
	public ConfigValue<Integer> REFRESH_COUNTRY_POWER_AFTER_OPEN;
	
	@Autowired
	private SimpleScheduler simpleScheduler;

	private Future<?> refreshCountryPower;

	private volatile Integer worldLevel = new Integer(1);

	private volatile Integer worldMilitaryRank = 0;

	private static final List<RankType> promotionEffect = Arrays.asList(RankType.QI_LEVEL, RankType.CHU_LEVEL, RankType.ZHAO_LEVEL,
			RankType.QI_ARTIFACT, RankType.CHU_ARTIFACT, RankType.ZHAO_ARTIFACT,  
			RankType.QI_HORSE, RankType.CHU_HORSE, RankType.ZHAO_HORSE, 
			RankType.QI_SOUL, RankType.CHU_SOUL, RankType.ZHAO_SOUL,
			RankType.QI_MILITARY, RankType.CHU_MILITARY, RankType.ZHAO_MILITARY, 
			RankType.QI_POWER, RankType.CHU_POWER, RankType.ZHAO_POWER, 
			RankType.QI_TREASURE, RankType.CHU_TREASURE, RankType.ZHAO_TREASURE,
			RankType.QI_PROTECTURE, RankType.CHU_PROTECTURE, RankType.ZHAO_PROTECTURE,
			RankType.QI_MEDAL, RankType.CHU_MEDAL, RankType.ZHAO_MEDAL, 
			RankType.ACTIVITY_CONSUME_TYPE, RankType.ACTIVITY_LEVEL_TYPE, RankType.ACTIVITY_HORSE,
			RankType.ACTIVITY_ENHANCEEQUIP, RankType.ACTIVITY_ARTIFACT, RankType.ACTIVITY_MILITARY, RankType.ACTIVITY_FIGHTPOWER,
			RankType.MERGE_ACTIVITY_CONSUME);

	@PostConstruct
	void init() {
		INSTANCE = this;
		List<WorldRankEnt> worldRankList = initAllRank(); // querier.all(WorldRankEnt.class);
		for (WorldRankEnt ent : worldRankList) {
			rankMap.put(RankType.valueOf(ent.getId()), ent.getWorldRank());
		}
		List<CountryRankEnt> countryRankList = initCountryRank(); // querier.all(CountryRankEnt.class);
		for (CountryRankEnt ent : countryRankList) {
			countryRankMap.put(ent.getId(), ent.getCountryRank());
		}
		List<OpenServerCountryRankEnt> openServerRankList = initOpenServerRank();
		for (OpenServerCountryRankEnt ent : openServerRankList) {
			openServerRankMap.put(ent.getId(), ent.getCountryRank());
		}

		// 0点的排行榜定时任务
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				refreshCountryHeroRank(true);				
			}
			
			@Override
			public String getName() {
				return "0点刷新国家英雄排行榜";
			}
		}, "0 0 0 * * *");

		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int oldWorldLevel = worldLevel;
				if (oldWorldLevel != calcWorldLevel()) {
					SessionManager.getInstance().sendAllIdentified(SM_World_Level.valueOf(worldLevel));
				}
			}

		}, DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_MINUTE);

		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int oldMilitaryRank = worldMilitaryRank;
				if (oldMilitaryRank != calcWorldMilitaryRank()) {
					SessionManager.getInstance().sendAllIdentified(SM_World_Military_Level.valueOf(worldMilitaryRank));
				}
			}
		}, DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);
		if (refreshCountryPower == null || refreshCountryPower.isCancelled()) {
			refreshCountryPower = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (ServerState.getInstance().isOpenServer()) {
						Date openDate = ServerState.getInstance().getOpenServerDate();
						long interval = ((System.currentTimeMillis() - openDate.getTime()) / DateUtils.MILLIS_PER_MINUTE);
						int dayInterval = DateUtils.calcIntervalDays(openDate, new Date());
						if (dayInterval >= 2) {
							refreshCountryPower.cancel(false);
						} else if (interval >= REFRESH_COUNTRY_POWER_AFTER_OPEN.getValue()) {
							calculateCountryLevel(true);
							refreshCountryPower.cancel(false);
						}
					}
				}
			}, DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_MINUTE);
		}
		calcWorldLevel();
		calcWorldMilitaryRank();
	}

	public int getWeakestCountry() {
		LinkedList<RankRow> elems = rankMap.get(RankType.COUNTRY_POWER).getRankRowsCopy();
		if (elems.isEmpty()) {
			return 0;
		} else {
			return (int) elems.get(elems.size() - 1).getObjId();
		}
	}

	private void refreshCountryHeroRank(boolean sendPack) {
		// 0点跨天的时刻，把今日英雄榜放到昨天
		putTodayHeroRank2Yesterday(RankType.QI_TODAY_HERO, RankType.QI_YESTERDAY_HERO, sendPack);
		putTodayHeroRank2Yesterday(RankType.CHU_TODAY_HERO, RankType.CHU_YESTERDAY_HERO, sendPack);
		putTodayHeroRank2Yesterday(RankType.ZHAO_TODAY_HERO, RankType.ZHAO_YESTERDAY_HERO, sendPack);
	}

	private LinkedList<RankRow> refreshCountryPowerByDailyRank() {
		// 0点 刷新国家实力排行榜
		if (ServerState.getInstance().isOpenServer()) {
			int interval = DateUtils.calcIntervalDays(ServerState.getInstance().getOpenServerDate(), new Date());
			if (interval < 2) { // 开服前两天 不计算国家实力
				return null;
			}
			LinkedList<RankRow> list = new LinkedList<RankRow>();
			for (Entry<Integer, CountryRank> entry : countryRankMap.entrySet()) {
				CountryRank countryRank = entry.getValue();
				long power = countryRank.refreshAndCalcPower();
				list.add(CountryRankElement.valueOf(entry.getKey(), power));
			}
			Collections.sort(list);
			rankMap.get(RankType.COUNTRY_POWER).setRankRowsBySpecified(list);
			return list;
		}
		return null;
	}

	private LinkedList<RankRow> refreshCountryPowerByOpenServer() {
		if (ServerState.getInstance().isOpenServer()) {
			LinkedList<RankRow> list = new LinkedList<RankRow>();
			for (Entry<Integer, OpenServerCountryRank> entry : openServerRankMap.entrySet()) {
				OpenServerCountryRank countryRank = entry.getValue();
				long power = countryRank.calcPower();
				list.add(CountryRankElement.valueOf(entry.getKey(), power));
			}
			Collections.sort(list);
			rankMap.get(RankType.COUNTRY_POWER).setRankRowsBySpecified(list);
			if (ServerState.getInstance().getFlagSpecifiedStatus() == FlagSpecifiedSatatus.NOT_CALCALCULATE.getValue()) {
				ServerState.getInstance().openServerCalcCountryPower();
			}
			return list;
		}
		return null;
	}

	public void calculateCountryLevel(boolean force) {
		LinkedList<RankRow> ranks = null;
		if (force) {
			ranks = refreshCountryPowerByOpenServer();
		} else {
			ranks = refreshCountryPowerByDailyRank();
		}
		if (ranks != null && !ranks.isEmpty()) {
			long mostCountryPower = 0L;
			for (int i = 0; i < ranks.size(); i++) {
				CountryRankElement ele = (CountryRankElement) ranks.get(i);
				if (ele.getPower() > mostCountryPower) {
					mostCountryPower = ele.getPower();
				}
			}
			for (int i = 0; i < ranks.size(); i++) {
				CountryRankElement ele = (CountryRankElement) ranks.get(i);
				Country c = CountryManager.getInstance().getCountries().get(CountryId.valueOf((int) ele.getObjId()));
				c.calcAndSetCountryLevel(ele.getPower(), mostCountryPower);
			}
		}
	}

	/**
	 * 计算世界等级
	 */
	private int calcWorldLevel() {
		int newWorldLevel = 0;
		double qiLevel = 0.0, chuLevel = 0.0, zhaoLevel = 0.0;
		qiLevel = calculateCountryLevel(rankMap.get(RankType.QI_LEVEL).getRankRowsCopy());
		chuLevel = calculateCountryLevel(rankMap.get(RankType.CHU_LEVEL).getRankRowsCopy());
		zhaoLevel = calculateCountryLevel(rankMap.get(RankType.ZHAO_LEVEL).getRankRowsCopy());
		newWorldLevel = (int) (Math.ceil(qiLevel + chuLevel + zhaoLevel) / 3);
		worldLevel = newWorldLevel;
		return worldLevel;
	}

	private int calcWorldMilitaryRank() {
		double military = 0.0;
		LinkedList<RankRow> rows = rankMap.get(RankType.MILITARY).getRankRowsCopy();
		for (CountryId countryId : CountryId.values()) {
			int maxLen = 0;
			double countryMilitary = 0.0;
			for (int i = 0; i < rows.size(); i++) {
				MilitaryRankElement e = (MilitaryRankElement) rows.get(i);
				if (e.getCountry() == countryId.getValue()) {
					if (maxLen >= 20) {
						break;
					}
					maxLen++;
					countryMilitary += e.getRank();
				}
			}
			military += Math.ceil(countryMilitary / maxLen);
		}
		worldMilitaryRank = (int) Math.ceil(military / CountryId.values().length);
		return worldMilitaryRank;
	}

	/**
	 * @return 世界等级
	 */
	public int getWorldLevel() {
		if (worldLevel == 0)
			return 1;
		return worldLevel;
	}

	/**
	 * @return 世界军衔等级
	 */
	public int getWorldMilitaryRank() {
		return worldMilitaryRank;
	}

	public void updateRank(WorldRank rank) {
		rankEntDbService.writeBack(rank.getRankType().name(), rank.getRankEnt());
	}

	public void updateCountryRank(CountryRank rank) {
		countryRankEntDBService.writeBack(rank.getCountryValue(), rank.getRankEnt());
	}

	public void updateOpenServerCountryRank(OpenServerCountryRank rank) {
		openServerEntDBService.writeBack(rank.getCountryValue(), rank.getRankEnt());
	}

	private List<WorldRankEnt> initAllRank() {
		List<WorldRankEnt> list = new LinkedList<WorldRankEnt>();
		for (final RankType type : RankType.values()) {
			list.add(rankEntDbService.loadOrCreate(type.name(), new EntityBuilder<String, WorldRankEnt>() {
				@Override
				public WorldRankEnt newInstance(String id) {
					return WorldRankEnt.valueOf(type);
				}
			}));
		}
		return list;
	}

	private List<CountryRankEnt> initCountryRank() {
		List<CountryRankEnt> list = new LinkedList<CountryRankEnt>();
		for (final CountryId cid : CountryId.values()) {
			list.add(countryRankEntDBService.loadOrCreate(cid.getValue(), new EntityBuilder<Integer, CountryRankEnt>() {
				@Override
				public CountryRankEnt newInstance(Integer id) {
					return CountryRankEnt.valueOf(id);
				}
			}));
		}
		return list;
	}

	private List<OpenServerCountryRankEnt> initOpenServerRank() {
		List<OpenServerCountryRankEnt> list = new LinkedList<OpenServerCountryRankEnt>();
		for (final CountryId cid : CountryId.values()) {
			list.add(openServerEntDBService.loadOrCreate(cid.getValue(),
					new EntityBuilder<Integer, OpenServerCountryRankEnt>() {
						@Override
						public OpenServerCountryRankEnt newInstance(Integer id) {
							return OpenServerCountryRankEnt.valueOf(id);
						}
					}));
		}
		return list;
	}

	public static WorldRankManager getInstance() {
		return INSTANCE;
	}

	/**
	 * 国家实力排行榜
	 * 
	 * @param countryValue
	 *            国家
	 * @param event
	 */
	public void submitCountryRankRow(int countryValue, BattleScoreRefreshEvent event) {
		countryRankMap.get(countryValue).battleScoreHandler(event);
	}

	public void submitOpenServerRankRow(int countryValue, BattleScoreRefreshEvent event) {
		Date openTime = ServerState.getInstance().getOpenServerDate();
		if (openTime != null) {
			long interval = ((System.currentTimeMillis() - openTime.getTime()) / DateUtils.MILLIS_PER_MINUTE);
			if (interval < REFRESH_COUNTRY_POWER_AFTER_OPEN.getValue()) {
				openServerRankMap.get(countryValue).battleScoreHandler(event);
			}
		}
	}

	public void submitRankRow(Player sender, RankType type, IEvent event) {
		if (sender.getOperatorPool().getGmPrivilege().isGm()) {
			return;
		}
		RankType rankType = type.getCountryRank(sender.getCountryValue());
		rankMap.get(rankType).submitRankRow(sender, event);
	}

	public void getRankByType(Player player, RankType type, int page) {
		if (page < 1) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		int start = (page - 1) * NUMS_PER_PAGE.getValue();
		int end = page * NUMS_PER_PAGE.getValue();

		if (type == RankType.QI_TODAY_HERO) {
			type = getHeroType(player.getCountryValue(), true);
		} else if (type == RankType.QI_YESTERDAY_HERO) {
			type = getHeroType(player.getCountryValue(), false);
		} else if (CompeteRankValue.findRank(type.getValue()) != null
				|| type.isCompeteRank()) {
			end = WorldRankManager.getInstance().MAX_SIZE_MAP.getValue().get(type.name());
		}

		rankMap.get(type).getRank(player, start, end);
	}

	public Map<Integer, RankRow> getHeroRank(Player player, boolean todayHero) {
		int countryValue = player.getCountryValue();
		RankType type = getHeroType(countryValue, todayHero);
		return rankMap.get(type).contains(player.getObjectId());
	}

	public RankType getHeroType(int countryValue, boolean todayHero) {
		RankType type = null;
		if (todayHero) {
			switch (countryValue) {
			case 1:
				type = RankType.QI_TODAY_HERO;
				break;
			case 2:
				type = RankType.CHU_TODAY_HERO;
				break;
			case 3:
				type = RankType.ZHAO_TODAY_HERO;
				break;
			}
		} else {
			switch (countryValue) {
			case 1:
				type = RankType.QI_YESTERDAY_HERO;
				break;
			case 2:
				type = RankType.CHU_YESTERDAY_HERO;
				break;
			case 3:
				type = RankType.ZHAO_YESTERDAY_HERO;
				break;
			}
		}
		return type;
	}

	public void rewardHero(Player player) {
		Map<Integer, RankRow> rank = getHeroRank(player, false);
		if (rank != null) {
			for (Entry<Integer, RankRow> entry : rank.entrySet()) {
				HeroRankElement e = (HeroRankElement) entry.getValue();
				if (!e.isReward() && entry.getKey() < NUMS_PER_PAGE.getValue()) {
					Reward reward = RewardManager.getInstance().creatReward(player,
							HERO_REWARD_MAP.getValue().get(String.valueOf(entry.getKey() + 1)), null);
					RewardManager.getInstance().grantReward(player, reward,
							ModuleInfo.valueOf(ModuleType.HERO_RANK, SubModuleType.YESTERDAY_HERO_REWARD));
					e.setReward(true);
					rankMap.get(getHeroType(player.getCountryValue(), false)).update();
				}
			}
		} else {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.PLAYER_NOT_IN_RANK);
		}
	}

	/**
	 * @param todayHero
	 * @param yesterdayHero
	 * @return 最新的昨日英雄榜
	 */
	private LinkedList<RankRow> putTodayHeroRank2Yesterday(RankType todayHero, RankType yesterdayHero, boolean sendUpdate) {
		// 0点跨天的时刻，把今日英雄榜放到昨天
		LinkedList<RankRow> data = rankMap.get(todayHero).getRankRowsCopy();
		if (sendUpdate) {
			sendUpdate(data);
		}
		rankMap.get(todayHero).setRankRowsBySpecified(new LinkedList<RankRow>());
		rankMap.get(yesterdayHero).setRankRowsBySpecified(data);
		long now = System.currentTimeMillis();
		rankMap.get(todayHero).setRefreshTime(now);
		rankMap.get(yesterdayHero).setRefreshTime(now);
		rankMap.get(todayHero).update();
		rankMap.get(yesterdayHero).update();
		return data;
	}

	private void sendUpdate(LinkedList<RankRow> data) {
		Map<String, I18nPack> top3Params = New.hashMap();
		// 先找出前三名
		for (int i = 0; i <= 2 && i < data.size(); i++) {
			HeroRankElement ele = (HeroRankElement) data.get(i);
			Player player = PlayerManager.getInstance().getPlayer(data.get(i).getObjId());
			if (player.getGang() != null) {
				ele.setGroup(player.getGang().getName());
			}
			if (!top3Params.containsKey("country")) {
				top3Params.put("country", I18nPack.valueOf(player.getCountry().getName()));
			}
			if (!top3Params.containsKey("content")) {
				top3Params.put("content", I18nPack.valueOf(HeroMailShow.createShow(ele, i + 1)));
			} else {
				top3Params.get("content").getObjects().add(HeroMailShow.createShow(ele, i + 1));
			}
		}

		I18nUtils title = I18nUtils.valueOf(YESTERDAY_HERO_MAIL_TITLE.getValue());
		for (int i = 0; i < data.size(); i++) {
			Player player = PlayerManager.getInstance().getPlayer(data.get(i).getObjId());
			I18nUtils content = I18nUtils.valueOf(YESTERDAY_HERO_MAIL_CONTENT.getValue());
			for (Entry<String, I18nPack> entry : top3Params.entrySet()) {
				content.getParms().put(entry.getKey(), entry.getValue().copyOf());
			}
			if (i > 2) {
				HeroRankElement ele = (HeroRankElement) data.get(i);
				if (player.getGang() != null) {
					ele.setGroup(player.getGang().getName());
				}
				content.getParms().get("content").getObjects().add(HeroMailShow.createShow(ele, i + 1));
			}
			if (i < NUMS_PER_PAGE.getValue()) {
				PacketSendUtility.sendPacket(player, new SM_Get_Hero_Reward());
			}
			Mail mail = Mail.valueOf(title, content, null, null);
			MailManager.getInstance().sendMail(mail, player.getObjectId());

		}
	}

	private double calculateCountryLevel(LinkedList<RankRow> rows) {
		double countryLevel = 0.0;
		int len = Math.min(rows.size(), 20);
		for (int i = 0; i < len; i++) {
			PlayerLevelRankElement e = (PlayerLevelRankElement) rows.get(i);
			countryLevel += e.getLevel();
		}
		return Math.ceil(countryLevel / len);
	}

	public void changeMyRankRowInfo(RankType type, IEvent event) {
		rankMap.get(type).changeRankElementByEvent(event);
	}

	public void searchCountryPowerPlayer(Player player) {
		int countryValue = player.getCountryValue();
		RankType type = RankType.COMPOSITE_BATTLESCORE.getCountryRank(countryValue);
		LinkedList<RankRow> rows = rankMap.get(type).getRankRowsCopy();
		ArrayList<RankRow> retPack = new ArrayList<RankRow>();
		for (RankRow row : rows) {
			if (!player.getCountry().isOffical(row.getObjId()) && retPack.size() < 50) {
				retPack.add(row);
			}
		}
		PacketSendUtility.sendPacket(player, SM_Country_Power_Player.valueOf(retPack));
	}

	public boolean yesterdayHeroReward(Player player) {
		Map<Integer, RankRow> row = getHeroRank(player, false);
		if (row == null) {
			return false;
		} else {
			for (Entry<Integer, RankRow> entry : row.entrySet()) {
				HeroRankElement e = (HeroRankElement) entry.getValue();
				return !e.isReward() && entry.getKey() < NUMS_PER_PAGE.getValue();
			}
		}
		return false;
	}

	public void refreshByPromotionEvent(PromotionEvent event) {
		for (RankType type : promotionEffect) {
			changeMyRankRowInfo(type, event);
		}
	}

	public boolean verifyInRank(Player player, RankType rankType, int high, int low) {
		WorldRank worldRank = rankMap.get(rankType);
		return worldRank.isPlayerInRankRange(player, high, low);
	}

	public int getMyRank(Player player, RankType type) {
		return rankMap.get(type).getMyRank(player.getObjectId());
	}

	public LinkedList<RankRow> getRankRowsCopy(RankType type) {
		return rankMap.get(type).getRankRowsCopy();
	}

	public int getPlayerWorldLevel(Player player) {
		int worldLevel = 0;
		if (player.getLevel() >= WorldRankManager.getInstance().WORLD_LEVEL_VALID_BASE.getValue()) {
			worldLevel = this.worldLevel;
		}
		return worldLevel;
	}

	public LinkedList<RankRow> getCountryLevelRank(CountryId countryId) {
		if (countryId == CountryId.C1) {
			return rankMap.get(RankType.QI_LEVEL).getRankRows();
		} else if (countryId == CountryId.C2) {
			return rankMap.get(RankType.CHU_LEVEL).getRankRows();
		} else if (countryId == CountryId.C3) {
			return rankMap.get(RankType.ZHAO_LEVEL).getRankRows();
		}
		return null;
	}
	
	public void updateAllRank() {
		for (Entry<RankType, WorldRank> entry : rankMap.entrySet()) {
			entry.getValue().update();
		}
		for (Entry<Integer, CountryRank> entry : countryRankMap.entrySet()) {
			entry.getValue().update();
		}
		for (Entry<Integer, OpenServerCountryRank> entry : openServerRankMap.entrySet()) {
			entry.getValue().update();
		}
	}
	
	public void initYesterDayRank() {
		if (!DateUtils.isToday(new Date(rankMap.get(RankType.QI_YESTERDAY_HERO).getRefreshTime()))) {
			refreshCountryHeroRank(false);
		}
	}
	
	public long getRankTypeRefreshTime(RankType type) {
		return rankMap.get(type).getRefreshTime();
	}
	
}
