package com.mmorpg.mir.model.country.model;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.capturetown.model.PlayerCaptureTownInfo;
import com.mmorpg.mir.model.country.event.ClawbackFlagFinishEvent;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

/**
 * 玩家参与国家活动相关信息
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-1
 * 
 */
public class PlayerCountryHistory {
	@Transient
	private transient Player owner;

	private long refreshTime; // 每日刷新的时间标记

	private NonBlockingHashMap<Integer, Integer> hiddenMissionDailyCount;
	
	private PlayerCaptureTownInfo captureTownInfo;

	private boolean attendFlag;

	public static PlayerCountryHistory valueOf(Player owner) {
		PlayerCountryHistory self = new PlayerCountryHistory();
		self.owner = owner;
		self.hiddenMissionDailyCount = new NonBlockingHashMap<Integer, Integer>();
		return self;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(refreshTime))) {
			refreshTime = System.currentTimeMillis();
			for (Entry<String, Integer> entry : ConfigValueManager.getInstance().HIDDEN_DAILY_MISSION.getValue()
					.entrySet()) {
				HiddenMissionType type = HiddenMissionType.valueOf(entry.getKey());
				int perCount = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(type.name());
				hiddenMissionDailyCount.put(type.getValue(), entry.getValue() * perCount);
			}
		}
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public NonBlockingHashMap<Integer, Integer> getHiddenMissionDailyCount() {
		return hiddenMissionDailyCount;
	}

	public void setHiddenMissionDailyCount(NonBlockingHashMap<Integer, Integer> hiddenMissionDailyCount) {
		this.hiddenMissionDailyCount = hiddenMissionDailyCount;
	}

	public boolean isAttendFlag() {
		return attendFlag;
	}

	public void setAttendFlag(boolean attendFlag) {
		this.attendFlag = attendFlag;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public boolean hiddenMissionFinished(HiddenMissionType type) {
		return hiddenMissionDailyCount.get(type.getValue()) <= 0;
	}

	@JsonIgnore
	public synchronized boolean takeMission(HiddenMissionType type) {
		if (hiddenMissionFinished(type)) {
			return false;
		}
		int leftCount = hiddenMissionDailyCount.get(type.getValue()) - 1;
		hiddenMissionDailyCount.put(type.getValue(), leftCount);
		int perCount = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(type.name());
		return leftCount % perCount == 0;
	}

	public synchronized void clawbackCountryMission(HiddenMissionType type, int count) {
		int perCount = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(type.name());
		hiddenMissionDailyCount.put(type.getValue(), hiddenMissionDailyCount.get(type.getValue()) + (count * perCount));
		if (type == HiddenMissionType.DEFEND_FLAG) {
			EventBusManager.getInstance().submit(ClawbackFlagFinishEvent.valueOf(owner.getObjectId(), count));
		}
	}

	@JsonIgnore
	public Map<Integer, Integer> getHiddenMissionInfo() {
		Map<Integer, Integer> map = New.hashMap();
		for (Entry<Integer, Integer> entry : hiddenMissionDailyCount.entrySet()) {
			if (entry.getKey() == 8) {
				continue;
			}
			HiddenMissionType type = HiddenMissionType.valueOf(entry.getKey().intValue());
			int perCount = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(type.name());
			map.put(entry.getKey(), (int) Math.ceil(entry.getValue() * 1.0 / perCount));
		}
		return map;
	}

	@JsonIgnore
	public int getMissionFinishCount(HiddenMissionType type) {
		int preCount = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(type.name());
		int dailyCount = ConfigValueManager.getInstance().HIDDEN_DAILY_MISSION.getValue().get(type.name());
		int currentLeftCount = hiddenMissionDailyCount.get(type.getValue());
		return (preCount * dailyCount - currentLeftCount) / preCount;
	}

	public PlayerCaptureTownInfo getCaptureTownInfo() {
		return captureTownInfo;
	}

	public void setCaptureTownInfo(PlayerCaptureTownInfo captureTownInfo) {
		this.captureTownInfo = captureTownInfo;
	}

}
