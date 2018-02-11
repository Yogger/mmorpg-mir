package com.mmorpg.mir.model.welfare.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.ClawDoneTodayNumEvent;
import com.mmorpg.mir.model.welfare.manager.ClawbackManager;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.windforce.common.event.core.EventBusManager;

/**
 * 收益追回
 * 
 * @author 37wan
 * 
 */
public class Clawback {

	private Map<Integer, ClawbackValue> map;

	@JsonIgnore
	public void clear(int eventId) {
		getClawbackValue(eventId).clear();
	}

	@JsonIgnore
	public void set(int eventId, ClawbackValue value) {
		ClawbackValue clawbackValue = new ClawbackValue();
		clawbackValue.setCurrentNum(value.getCurrentNum());
		clawbackValue.setCurrentRuns(value.getCurrentRuns());
		clawbackValue.setLastTime(value.getLastTime());
		clawbackValue.setOpenTime(value.getOpenTime());
		clawbackValue.setCurrentClawbackNum(value.getCurrentClawbackNum());
		initMap().put(eventId, clawbackValue);
	}

	@JsonIgnore
	public void addNum(Player player, ClawbackResource resource, int eventId, int count) {
		ClawbackValue clawbackValue = getClawbackValue(eventId);
		int num = clawbackValue.getCurrentNum() + count;
		int maxNums = ClawbackManager.getInstance().getCorrectExeNums(resource, player.getVip().getLevel());
		int currentDayClaw = player.getWelfare().getWelfareHistory().getClawbackNum(ClawbackEnum.valueOf(resource.getEventId()));
		if (clawbackValue.getCurrentClawbackNum() < currentDayClaw) {
			clawbackValue.logUseClawbackNum(count);
		} else {
			num = num > maxNums ? maxNums : num;
			clawbackValue.setCurrentNum(num);
			EventBusManager.getInstance().submit(ClawDoneTodayNumEvent.valueOf(player, ClawbackEnum.valueOf(resource.getEventId()).getRecollectType()));
		}
		clawbackValue.setLastTime(System.currentTimeMillis());
		initMap().put(eventId, clawbackValue);
	}

	@JsonIgnore
	public void addRun(Player player, ClawbackResource resource, int eventId, int count) {
		ClawbackValue clawbackValue = getClawbackValue(eventId);
		int runs = clawbackValue.getCurrentRuns() + count;
		int maxRuns = ClawbackManager.getInstance().getCorrectExeRuns(resource, player.getVip().getLevel());
		runs = runs > maxRuns ? maxRuns : runs;
		clawbackValue.setCurrentRuns(runs);
		clawbackValue.setLastTime(System.currentTimeMillis());
		initMap().put(eventId, clawbackValue);
	}

	@JsonIgnore
	public int getExeNum(int eventId) {
		return getClawbackValue(eventId).getCurrentNum();
	}

	@JsonIgnore
	public int getRunNum(int eventId) {
		return getClawbackValue(eventId).getCurrentRuns();
	}

	@JsonIgnore
	public long getLastExeTime(int eventId) {
		return getClawbackValue(eventId).getLastTime();
	}

	@JsonIgnore
	public ClawbackValue getClawbackValue(int eventId) {
		return initMap().get(eventId);
	}

	@JsonIgnore
	public boolean isOpen(int eventId) {
		ClawbackValue clawbackValue = getClawbackValue(eventId);
		return (clawbackValue.getOpenTime() > 0);
	}

	@JsonIgnore
	public void open(int eventId) {
		ClawbackValue clawbackValue = getClawbackValue(eventId);
		if (clawbackValue.getOpenTime() == 0) {
			clawbackValue.setOpenTime(System.currentTimeMillis());
		}
	}

	@JsonIgnore
	public long getOpenTime(int eventId) {
		return getClawbackValue(eventId).getOpenTime();
	}

	public Map<Integer, ClawbackValue> getMap() {
		return map;
	}

	public void setMap(Map<Integer, ClawbackValue> map) {
		this.map = map;
	}

	@JsonIgnore
	synchronized private Map<Integer, ClawbackValue> initMap() {
		if (this.map == null || this.map.isEmpty()) {
			Map<Integer, ClawbackValue> map = New.hashMap();
			for (ClawbackEnum claw : ClawbackEnum.values()) {
				map.put(claw.getEventId(), new ClawbackValue());
			}
			this.map = map;
		}
		return this.map;
	}

	public void print() {
		for (Map.Entry<Integer, ClawbackValue> entry : map.entrySet()) {
			System.out.println("key : " + entry.getKey());
			System.out.println("value : " + entry.getValue());
		}
	}
}
