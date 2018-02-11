package com.mmorpg.mir.model.serverstate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("2m"))
public class ServerStateEnt implements IEntity<Integer> {

	@Id
	private int id;
	/** 开服时间 */
	private Date openServerDate;
	/** 最后一次开皇城战 */
	private Date lastOpenKingOfWarDate;
	/** 最后一次开家族战 */
	private Date lastOpenGangOfWarDate;
	/** 怪物击杀记录 */
	@Lob
	private String monsterKillHisJson;
	/** 最近一次击杀怪物记录 */
	private Date lastKillMonterDate;
	/** 最近一次发每日充值没领取奖励的时间 */
	private Date lastEveryDayCompensate;
	/** 最近一次怪物攻城活动结束的时间 */
	private Date lastQiMonsterRiotDate;
	/** 最近一次怪物攻城活动结束的时间 */
	private Date lastChuMonsterRiotDate;
	/** 最近一次怪物攻城活动结束的时间 */
	private Date lastZhaoMonsterRiotDate;
	/** 最近一次怪物攻城活动结束的时间 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int countryPowerFlagQuest;
	/** 暴击活动开启次数 */
	private Integer weekCriOpenCount;
	/** 暴击活动增加次数的最后时间 */
	private Date weekCriLastCheckTime;
	/** 服务器限量的东西 */
	@Lob
	private String serverLimitJson;
	@Lob
	private String levelRewardJson;
	@Lob
	private String levelRewardLogJson;
	@Lob
	private String attendRiotLogJson;
	@Lob
	private String groupPurchasePlayersJson;
	@Lob
	private String playerGroupPurchaseJson;
	/** 超值团购2 */
	@Lob
	private String groupPurchasePlayersJson2;
	@Lob
	private String playerGroupPurchaseJson2;
	@Lob
	private String groupPurchasePlayersJson3;
	@Lob
	private String playerGroupPurchaseJson3;
	@Lob
	private String qiHu360PrivilegeServerJson;
	@Lob
	private String qiHu360SpeedPrivilegeServerJson;
	@Lob
	private String celebrateActivityInfoJson;
	@Lob
	private String rechargeCelebrateJson;
	@Lob
	private String blackShopServerJson;
	@Lob
	private String commonIdentifyTreasureTotalServersJson;
	@Lob
	private String goldTreasuryServersJson;
	@Column(columnDefinition = "int default 0", nullable = false)
	private volatile int assassinLevel;
	@Column(columnDefinition = "int default 0", nullable = false)
	private volatile int ministerLevel;
	/**
	 * 已经合并的服
	 */
	@Lob
	private String mergeServersJson;
	/** 最近一次合服的时间 */
	private Date lastMergeTime;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public boolean serialize() {
		if (ServerState.getInstance().getServerLimitMap() != null) {
			serverLimitJson = JsonUtils.object2String(ServerState.getInstance().getServerLimitMap());
		}
		if (ServerState.getInstance().getLevelRewardMap() != null) {
			levelRewardJson = JsonUtils.object2String(ServerState.getInstance().getLevelRewardMap());
		}
		if (ServerState.getInstance().getLevelRewardLog() != null) {
			levelRewardLogJson = JsonUtils.object2String(ServerState.getInstance().getLevelRewardLog());
		}
		if (ServerState.getInstance().getAttendRiotLog() != null) {
			attendRiotLogJson = JsonUtils.object2String(ServerState.getInstance().getAttendRiotLog());
		}
		if (ServerState.getInstance().getGroupPurchasePlayers() != null) {
			groupPurchasePlayersJson = JsonUtils.object2String(ServerState.getInstance().getGroupPurchasePlayers());
		}
		if (ServerState.getInstance().getPlayerGroupPurchases() != null) {
			playerGroupPurchaseJson = JsonUtils.object2String(ServerState.getInstance().getPlayerGroupPurchases());
		}
		if (ServerState.getInstance().getGroupPurchasePlayers2() != null) {
			groupPurchasePlayersJson2 = JsonUtils.object2String(ServerState.getInstance().getGroupPurchasePlayers2());
		}
		if (ServerState.getInstance().getPlayerGroupPurchases2() != null) {
			playerGroupPurchaseJson2 = JsonUtils.object2String(ServerState.getInstance().getPlayerGroupPurchases2());
		}
		if (ServerState.getInstance().getGroupPurchasePlayers3() != null) {
			groupPurchasePlayersJson3 = JsonUtils.object2String(ServerState.getInstance().getGroupPurchasePlayers3());
		}
		if (ServerState.getInstance().getPlayerGroupPurchases3() != null) {
			playerGroupPurchaseJson3 = JsonUtils.object2String(ServerState.getInstance().getPlayerGroupPurchases3());
		}
		if (ServerState.getInstance().getQiHu360PrivilegeServer() != null) {
			qiHu360PrivilegeServerJson = JsonUtils.object2String(ServerState.getInstance().getQiHu360PrivilegeServer());
		}
		if (ServerState.getInstance().getCelebrateActivityInfos() != null) {
			celebrateActivityInfoJson = JsonUtils.object2String(ServerState.getInstance().getCelebrateActivityInfos());
		}
		if (ServerState.getInstance().getCelebrateRecharge() != null) {
			rechargeCelebrateJson = JsonUtils.object2String(ServerState.getInstance().getCelebrateRecharge());
		}
		if (ServerState.getInstance().getQiHu360SpeedPrivilegeServer() != null) {
			qiHu360SpeedPrivilegeServerJson = JsonUtils.object2String(ServerState.getInstance()
					.getQiHu360SpeedPrivilegeServer());
		}
		if (ServerState.getInstance().getBlackShopServer() != null) {
			blackShopServerJson = JsonUtils.object2String(ServerState.getInstance().getBlackShopServer());
		}
		if (ServerState.getInstance().getCommonIdentifyTreasureTotalServers() != null) {
			commonIdentifyTreasureTotalServersJson = JsonUtils.object2String(ServerState.getInstance()
					.getCommonIdentifyTreasureTotalServers());
		}
		if (ServerState.getInstance().getGoldTreasuryServers() != null) {
			goldTreasuryServersJson = JsonUtils.object2String(ServerState.getInstance().getGoldTreasuryServers());
		}
		return true;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOpenServerDate() {
		return openServerDate;
	}

	public void setOpenServerDate(Date openServerDate) {
		this.openServerDate = openServerDate;
	}

	public Date getLastOpenKingOfWarDate() {
		return lastOpenKingOfWarDate;
	}

	public void setLastOpenKingOfWarDate(Date lastOpenKingOfWarDate) {
		this.lastOpenKingOfWarDate = lastOpenKingOfWarDate;
	}

	public Date getLastOpenGangOfWarDate() {
		return lastOpenGangOfWarDate;
	}

	public void setLastOpenGangOfWarDate(Date lastOpenGangOfWarDate) {
		this.lastOpenGangOfWarDate = lastOpenGangOfWarDate;
	}

	public String getMonsterKillHisJson() {
		return monsterKillHisJson;
	}

	public void setMonsterKillHisJson(String monsterKillHisJson) {
		this.monsterKillHisJson = monsterKillHisJson;
	}

	public Date getLastKillMonterDate() {
		return lastKillMonterDate;
	}

	public void setLastKillMonterDate(Date lastKillMonterDate) {
		this.lastKillMonterDate = lastKillMonterDate;
	}

	public String getServerLimitJson() {
		return serverLimitJson;
	}

	public void setServerLimitJson(String serverLimitJson) {
		this.serverLimitJson = serverLimitJson;
	}

	public Date getLastEveryDayCompensate() {
		return lastEveryDayCompensate;
	}

	public void setLastEveryDayCompensate(Date lastEveryDayCompensate) {
		this.lastEveryDayCompensate = lastEveryDayCompensate;
	}

	public String getLevelRewardJson() {
		return levelRewardJson;
	}

	public void setLevelRewardJson(String levelRewardJson) {
		this.levelRewardJson = levelRewardJson;
	}

	public String getLevelRewardLogJson() {
		return levelRewardLogJson;
	}

	public void setLevelRewardLogJson(String levelRewardLogJson) {
		this.levelRewardLogJson = levelRewardLogJson;
	}

	public String getAttendRiotLogJson() {
		return attendRiotLogJson;
	}

	public void setAttendRiotLogJson(String attendRiotLogJson) {
		this.attendRiotLogJson = attendRiotLogJson;
	}

	public Date getLastQiMonsterRiotDate() {
		return lastQiMonsterRiotDate;
	}

	public void setLastQiMonsterRiotDate(Date lastQiMonsterRiotDate) {
		this.lastQiMonsterRiotDate = lastQiMonsterRiotDate;
	}

	public Date getLastChuMonsterRiotDate() {
		return lastChuMonsterRiotDate;
	}

	public void setLastChuMonsterRiotDate(Date lastChuMonsterRiotDate) {
		this.lastChuMonsterRiotDate = lastChuMonsterRiotDate;
	}

	public Date getLastZhaoMonsterRiotDate() {
		return lastZhaoMonsterRiotDate;
	}

	public void setLastZhaoMonsterRiotDate(Date lastZhaoMonsterRiotDate) {
		this.lastZhaoMonsterRiotDate = lastZhaoMonsterRiotDate;
	}

	public int getCountryPowerFlagQuest() {
		return countryPowerFlagQuest;
	}

	public void setCountryPowerFlagQuest(int countryPowerFlagQuest) {
		this.countryPowerFlagQuest = countryPowerFlagQuest;
	}

	public String getGroupPurchasePlayersJson() {
		return groupPurchasePlayersJson;
	}

	public void setGroupPurchasePlayersJson(String groupPurchasePlayersJson) {
		this.groupPurchasePlayersJson = groupPurchasePlayersJson;
	}

	public String getPlayerGroupPurchaseJson() {
		return playerGroupPurchaseJson;
	}

	public void setPlayerGroupPurchaseJson(String playerGroupPurchaseJson) {
		this.playerGroupPurchaseJson = playerGroupPurchaseJson;
	}

	public String getQiHu360PrivilegeServerJson() {
		return qiHu360PrivilegeServerJson;
	}

	public void setQiHu360PrivilegeServerJson(String qiHu360PrivilegeServerJson) {
		this.qiHu360PrivilegeServerJson = qiHu360PrivilegeServerJson;
	}

	public String getGroupPurchasePlayersJson2() {
		return groupPurchasePlayersJson2;
	}

	public void setGroupPurchasePlayersJson2(String groupPurchasePlayersJson2) {
		this.groupPurchasePlayersJson2 = groupPurchasePlayersJson2;
	}

	public String getPlayerGroupPurchaseJson2() {
		return playerGroupPurchaseJson2;
	}

	public void setPlayerGroupPurchaseJson2(String playerGroupPurchaseJson2) {
		this.playerGroupPurchaseJson2 = playerGroupPurchaseJson2;
	}

	public String getGroupPurchasePlayersJson3() {
		return groupPurchasePlayersJson3;
	}

	public void setGroupPurchasePlayersJson3(String groupPurchasePlayersJson3) {
		this.groupPurchasePlayersJson3 = groupPurchasePlayersJson3;
	}

	public String getPlayerGroupPurchaseJson3() {
		return playerGroupPurchaseJson3;
	}

	public void setPlayerGroupPurchaseJson3(String playerGroupPurchaseJson3) {
		this.playerGroupPurchaseJson3 = playerGroupPurchaseJson3;
	}

	public String getCelebrateActivityInfoJson() {
		return celebrateActivityInfoJson;
	}

	public void setCelebrateActivityInfoJson(String celebrateActivityInfoJson) {
		this.celebrateActivityInfoJson = celebrateActivityInfoJson;
	}

	public String getRechargeCelebrateJson() {
		return rechargeCelebrateJson;
	}

	public void setRechargeCelebrateJson(String rechargeCelebrateJson) {
		this.rechargeCelebrateJson = rechargeCelebrateJson;
	}

	public String getQiHu360SpeedPrivilegeServerJson() {
		return qiHu360SpeedPrivilegeServerJson;
	}

	public void setQiHu360SpeedPrivilegeServerJson(String qiHu360SpeedPrivilegeServerJson) {
		this.qiHu360SpeedPrivilegeServerJson = qiHu360SpeedPrivilegeServerJson;
	}

	public String getMergeServersJson() {
		return mergeServersJson;
	}

	public void setMergeServersJson(String mergeServersJson) {
		this.mergeServersJson = mergeServersJson;
	}

	public Date getLastMergeTime() {
		return lastMergeTime;
	}

	public void setLastMergeTime(Date lastMergeTime) {
		this.lastMergeTime = lastMergeTime;
	}

	public String getBlackShopServerJson() {
		return blackShopServerJson;
	}

	public void setBlackShopServerJson(String blackShopServerJson) {
		this.blackShopServerJson = blackShopServerJson;
	}

	public Integer getWeekCriOpenCount() {
		return weekCriOpenCount;
	}

	public void setWeekCriOpenCount(Integer weekCriOpenCount) {
		this.weekCriOpenCount = weekCriOpenCount;
	}

	public Date getWeekCriLastCheckTime() {
		return weekCriLastCheckTime;
	}

	public void setWeekCriLastCheckTime(Date weekCriLastCheckTime) {
		this.weekCriLastCheckTime = weekCriLastCheckTime;
	}

	public String getCommonIdentifyTreasureTotalServersJson() {
		return commonIdentifyTreasureTotalServersJson;
	}

	public void setCommonIdentifyTreasureTotalServersJson(String commonIdentifyTreasureTotalServersJson) {
		this.commonIdentifyTreasureTotalServersJson = commonIdentifyTreasureTotalServersJson;
	}

	public String getGoldTreasuryServersJson() {
		return goldTreasuryServersJson;
	}

	public void setGoldTreasuryServersJson(String goldTreasuryServersJson) {
		this.goldTreasuryServersJson = goldTreasuryServersJson;
	}

	public int getAssassinLevel() {
		return assassinLevel;
	}

	public void setAssassinLevel(int assassinLevel) {
		this.assassinLevel = assassinLevel;
	}

	public int getMinisterLevel() {
		return ministerLevel;
	}

	public void setMinisterLevel(int ministerLevel) {
		this.ministerLevel = ministerLevel;
	}

}
