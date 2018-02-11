package com.mmorpg.mir.model.military.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.MilitaryStatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.military.event.MilitaryStrategyUpEvent;
import com.mmorpg.mir.model.military.manager.MilitaryManager;
import com.mmorpg.mir.model.military.packet.SM_Military_Change_BroadCast;
import com.mmorpg.mir.model.military.packet.SM_military_update;
import com.mmorpg.mir.model.military.packet.SM_strategy_update;
import com.mmorpg.mir.model.military.resource.MilitaryLevelResource;
import com.mmorpg.mir.model.military.resource.MilitaryStrategyResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class Military {

	public static final StatEffectId MILITARY_STAT = MilitaryStatEffectId.valueOf("MILITARY", StatEffectType.MILITARY);
	public static final StatEffectId STAR_STAT = MilitaryStatEffectId.valueOf("MILITARY_STAR", StatEffectType.MILITARY);
	public static final MilitaryStatEffectId[] MILITARY_STAT_STRATEGY = {

	MilitaryStatEffectId.valueOf("STRATEGY_I", 1), MilitaryStatEffectId.valueOf("STRATEGY_II", 2),
			MilitaryStatEffectId.valueOf("STRATEGY_III", 3) };

	public static final StatEffectId WAR_ACTIVITY_STAT = MilitaryStatEffectId.valueOf("WAR_ACTIVITY",
			StatEffectType.MILITARY);

	private Player owner;

	private volatile int rank;

	private String starId;

	private long lastStarActionTime;

	/** <playerId, <lastKillTime, count>> 防刷荣誉 */
	private transient Map<Long, Long> killLastTime;

	private transient Map<Long, Integer> killCount;

	private Map<Integer, Integer> strategy;

	/** 当前军衔兵法失败数  key:章节     value:次数 */
	private Map<Integer,Integer> failCount;

	/** 记录玩家进阶的时间 */
	private Map<Integer, Long> upgradeTimeLog;
	/** 记录玩家进阶的时的装备 */
	private Map<Integer, Map<Integer, String>> upgradeEquipLog;

	/** 今日获得的杀人荣誉 */
	private int todayHonor;

	/** 荣誉更新时间 */
	private long lastHonorRefreshTime;
	
	/** 记录突破*/
	private ArrayList<Integer> breaks;


	public ArrayList<Integer> getBreaks() {
		return breaks;
	}

	public void setBreaks(ArrayList<Integer> breaks) {
		this.breaks = breaks;
	}

	/** 返回真正添加的荣誉值值 */
	public int addTodayHonor(int honor, int todayHonorLimit) {
		int realAddHonor = honor;
		if (this.todayHonor + honor > todayHonorLimit) {
			realAddHonor = todayHonorLimit - this.todayHonor;
		}
		this.todayHonor += realAddHonor;
		return realAddHonor;
	}

	public void initMilitaryStat() {
		MilitaryLevelResource resource = MilitaryManager.getInstance().getResource(rank);

		// 兵法
		for (MilitaryStatEffectId id : MILITARY_STAT_STRATEGY) {
			Integer resourceId = getStrategyLevelBySection(id.getSection())
					+ MilitaryManager.getInstance().getInitalId(id.getSection());
			owner.getGameStats().addModifiers(id,
					MilitaryManager.getInstance().getStrategyResource(resourceId).getRoleStat(owner.getRole()), false);
		}
		// 军衔
		owner.getGameStats().addModifiers(Military.MILITARY_STAT, resource.getMilitaryStats(), false);
		
		refresh();
		
		// 星星
		if (!MilitaryManager.getInstance().checkNeedCost(owner)) {
			return;
		}
	
		setStarId(MilitaryManager.getInstance().doCostDailyHonor(owner));
		MilitaryManager.getInstance().refreshPlayerMilitaryStarBuff(owner);
	}

	public int refreshAndGetTodayHonor() {
		if (!DateUtils.isToday(new Date(lastHonorRefreshTime))) {
			this.todayHonor = 0;
			this.lastHonorRefreshTime = System.currentTimeMillis();
		}
		return this.todayHonor;
	}

	public void setTodayHonor(int todayHonor) {
		this.todayHonor = todayHonor;
	}

	public long getLastHonorRefreshTime() {
		return lastHonorRefreshTime;
	}

	public void setLastHonorRefreshTime(long lastHonorRefreshTime) {
		this.lastHonorRefreshTime = lastHonorRefreshTime;
	}

	/**
	 * @param option
	 *            true 表示开启活动额外属性，false 表示关闭活动额外属性
	 */
	public void buttonTheWarActivityStat(boolean option) {
		if (option) {
			owner.getGameStats().addModifiers(WAR_ACTIVITY_STAT,
					MilitaryManager.getInstance().getResource(rank).getWarStats(), true);
		} else {
			owner.getGameStats().endModifiers(WAR_ACTIVITY_STAT);
		}
	}

	public static Military valueOf(int rank) {
		Military military = new Military();
		military.rank = rank;
		military.killLastTime = new HashMap<Long, Long>();
		military.killCount = new HashMap<Long, Integer>();
		military.upgradeTimeLog = new HashMap<Integer, Long>();
		military.strategy = New.hashMap();
		military.upgradeEquipLog = New.hashMap();
		military.starId = MilitaryManager.getInstance().INITIAL_STAR_ID.getValue();
		military.failCount = New.hashMap();
		military.breaks = new ArrayList<Integer>();
		return military;
	}

	@JsonIgnore
	public void refresh() {
		long now = System.currentTimeMillis();
		ArrayList<Long> removeIds = New.arrayList();
		for (Entry<Long, Long> entry : killLastTime.entrySet()) {
			if (now - entry.getValue() >= DateUtils.MILLIS_PER_HOUR) {
				removeIds.add(entry.getKey());
			}
		}
		for (Long rId : removeIds) {
			killLastTime.remove(rId);
			killCount.remove(rId);
		}
	}

	public Map<Integer, Long> getUpgradeTimeLog() {
		return upgradeTimeLog;
	}

	public void setUpgradeTimeLog(Map<Integer, Long> upgradeTimeLog) {
		this.upgradeTimeLog = upgradeTimeLog;
	}

	public Map<Long, Long> getKillLastTime() {
		return killLastTime;
	}

	public void setKillLastTime(Map<Long, Long> killLastTime) {
		this.killLastTime = killLastTime;
	}

	public Map<Long, Integer> getKillCount() {
		return killCount;
	}

	public void setKillCount(Map<Long, Integer> killCount) {
		this.killCount = killCount;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void upgradeRank() {
		rank++;
		long now = System.currentTimeMillis();
		upgradeTimeLog.put(rank, now);
		Map<Integer, String> snapShot = new HashMap<Integer, String>(owner.getEquipmentIds());
		upgradeEquipLog.put(rank, snapShot);
		PacketSendUtility.sendPacket(owner, SM_military_update.valueOf(rank, now, snapShot));
		PacketSendUtility.broadcastPacket(owner, SM_Military_Change_BroadCast.valueOf(owner.getObjectId(), rank));

		EventBusManager.getInstance().submit(MilitaryRankUpEvent.valueOf(owner.getObjectId(), rank));
	}

	/**
	 * 增加军衔兵法失败次数
	 */
	public void upgrateFail(int section) {
		int failNum = failCount.get(section)+1;
		this.failCount.put(section, failNum);
	}

	
	public void clearFailCount(int section){
		this.failCount.put(section, 0);
	}
	
	public int upgradeStrategy(int id, int section) {
		MilitaryStrategyResource resource = MilitaryManager.getInstance().getStrategyResource(id);
		clearFailCount(section);
		int ret = 0;
		if ((ret = resource.getNextId()) != 0) {
			strategy.put(resource.getSection(), ret - MilitaryManager.getInstance().getInitalId(section));
			PacketSendUtility.sendPacket(owner, SM_strategy_update.valueOf(0, strategy, false, breaks));

			EventBusManager.getInstance().submit(
					MilitaryStrategyUpEvent.valueOf(owner.getObjectId(), section, strategy.get(section)));
		} else {
			PacketSendUtility.sendErrorMessage(owner, ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL);
		}
		return ret;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Map<Integer, Integer> getStrategy() {
		return strategy;
	}

	public void setStrategy(Map<Integer, Integer> strategy) {
		this.strategy = strategy;
	}

	public Map<Integer, Map<Integer, String>> getUpgradeEquipLog() {
		return upgradeEquipLog;
	}

	public void setUpgradeEquipLog(Map<Integer, Map<Integer, String>> upgradeEquipLog) {
		this.upgradeEquipLog = upgradeEquipLog;
	}

	@JsonIgnore
	public Integer getStrategyLevelBySection(int section) {
		Integer level = this.strategy.get(section);
		if (level == null) {
			this.strategy.put(section, 0);
			return 0;
		}
		return level;
	}

	public String getStarId() {
		return starId;
	}

	public void setStarId(String starId) {
		this.starId = starId;
	}

	public MilitaryVO createVO() {
		return MilitaryVO.valueOf(this);
	}

	public long getLastStarActionTime() {
		return lastStarActionTime;
	}

	public void setLastStarActionTime(long lastStarActionTime) {
		this.lastStarActionTime = lastStarActionTime;
	}

	public Map<Integer, Integer> getFailCount() {
		return failCount;
	}

	public void setFailCount(Map<Integer, Integer> failCount) {
		this.failCount = failCount;
	}
	
	@JsonIgnore
	public MilitaryLevelResource getResource() {
		return MilitaryManager.getInstance().getResource(rank);
	}
}
