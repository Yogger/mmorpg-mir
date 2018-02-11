package com.mmorpg.mir.model.welfare.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.windforce.common.utility.DateUtils;

/**
 * 在线奖励
 * 
 * @author 37wan
 * 
 */
public class OnlineReward {

	private long toDayOnlineTimes; // 今日累计在线时间
	private long lastRefreshTime; // 上一次刷新的时间
	private ArrayList<Integer> rewardedList = New.arrayList();// 已经领奖的档
	private Map<Integer, ArrayList<String>> rewardIdMap = New.hashMap();// 保存玩家领取过的奖励

	/** 累计在线时间 */
	@JsonIgnore
	public void addOnlineTimes(long times) {
		toDayOnlineTimes += times;
	}

	@JsonIgnore
	public long refreshTime(Player player) {
		Date now = new Date();
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk32")) {
			return 0L;
		}
		if (lastRefreshTime == 0L) {
			lastRefreshTime = now.getTime();
		}
		long loginTime = player.getPlayerStat().getLastLogin().getTime();
		long onlineStartTime = 0L;
		if (!DateUtils.isToday(new Date(lastRefreshTime))) { // 换了一天
			clear(); // 清除在线数据
			onlineStartTime = DateUtils.getFirstTime(now).getTime();
		}
		onlineStartTime = Math.max(onlineStartTime, loginTime);
		onlineStartTime = Math.max(lastRefreshTime, onlineStartTime);  
		toDayOnlineTimes += (now.getTime() - onlineStartTime);
		lastRefreshTime = now.getTime();
		return toDayOnlineTimes;
	}

	@JsonIgnore
	public boolean lackOnlinetime(long time) {
		return toDayOnlineTimes < time;
	}

	/** 是否已经领奖 */
	@JsonIgnore
	public boolean isRewarded(int index) {
		return rewardedList.contains(new Integer(index));
	}

	/** 领奖并记录 */
	@JsonIgnore
	public void rewarded(int index, ArrayList<String> rewardIds) {
		rewardedList.add(index);
		rewardIdMap.put(index, rewardIds);
	}

	public void clear() {
		rewardedList.clear();
		rewardIdMap.clear();
		toDayOnlineTimes = 0;
	}

	public long getToDayOnlineTimes() {
		return toDayOnlineTimes;
	}

	public void setToDayOnlineTimes(long toDayOnlineTimes) {
		this.toDayOnlineTimes = toDayOnlineTimes;
	}

	public ArrayList<Integer> getRewardedList() {
		return rewardedList;
	}

	public void setRewardedList(ArrayList<Integer> rewardedList) {
		this.rewardedList = rewardedList;
	}

	public Map<Integer, ArrayList<String>> getRewardIdMap() {
		return rewardIdMap;
	}

	public void setRewardIdMap(Map<Integer, ArrayList<String>> rewardIdMap) {
		this.rewardIdMap = rewardIdMap;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

}
