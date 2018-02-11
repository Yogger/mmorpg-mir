package com.mmorpg.mir.model.welfare.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.rank.model.DayKey;
import com.windforce.common.utility.DateUtils;

public class WelfareHistory {

	private Map<Integer, Integer> clawbackMap = New.hashMap();// 保存玩家今日追回的次数
	private Map<Integer, Long> lastClawbackTimeMap = New.hashMap();// 最后追回的时间
	private long intoLadder;// 进入过爬塔的时间
	private Map<Long, Integer> lastRestTime = New.hashMap(); // 最近几次重置爬塔副本的时间
	
	@JsonIgnore
	public void refresh() {
		long now = DayKey.valueOf().getLunchTime();// ;System.currentTimeMillis()
		List<Long> keys = New.arrayList();
		for (Long time : lastRestTime.keySet()) {
			if (now - time > 3 * org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY) {
				keys.add(time);
			}
		}
		for (Long id: keys) {
			lastRestTime.remove(id);
		}
	}
	
	@JsonIgnore
	public boolean intoLadder() {
		if (intoLadder == 0L) {
			intoLadder = System.currentTimeMillis();
			return true; 
		}
		return false;
	}

	/** 记录追回 */
	@JsonIgnore
	public void clawback(ClawbackEnum claw, int num) {
		release(claw);
		Integer n = clawbackMap.get(claw.getEventId());
		clawbackMap.put(claw.getEventId(), n == null ? num : num + n);
		lastClawbackTimeMap.put(claw.getEventId(), System.currentTimeMillis());
	}

	/** 获取当天追回次数 */
	@JsonIgnore
	public int getClawbackNum(ClawbackEnum claw) {
		release(claw);
		return clawbackMap.containsKey(claw.getEventId()) ? clawbackMap.get(claw.getEventId()) : 0;
	}

	@JsonIgnore
	private void release(ClawbackEnum claw) {
		long lastClawbackTime = lastClawbackTimeMap.containsKey(claw.getEventId()) ? lastClawbackTimeMap.get(claw
				.getEventId()) : 0;
		// 过期
		if (DateUtils.calcIntervalDays(new Date(lastClawbackTime), new Date()) > 0) {
			// 清理
			clawbackMap.remove(claw.getEventId());
			lastClawbackTimeMap.remove(claw.getEventId());
		}
	}

	public Map<Integer, Integer> getClawbackMap() {
		return clawbackMap;
	}

	public void setClawbackMap(Map<Integer, Integer> clawbackMap) {
		this.clawbackMap = clawbackMap;
	}

	public Map<Integer, Long> getLastClawbackTimeMap() {
		return lastClawbackTimeMap;
	}

	public void setLastClawbackTimeMap(Map<Integer, Long> lastClawbackTimeMap) {
		this.lastClawbackTimeMap = lastClawbackTimeMap;
	}

	@JsonIgnore
	public boolean isPass() {
		Long key = DayKey.valueOf().getLunchTime() - DateUtils.MILLIS_PER_DAY;
		boolean isIntoLadder = intoLadder != 0L && (!DateUtils.isToday(new Date(intoLadder)));
		return isIntoLadder && !lastRestTime.containsKey(key);
	}

	public long getIntoLadder() {
		return intoLadder;
	}

	public void setIntoLadder(long intoLadder) {
		this.intoLadder = intoLadder;
	}

	public Map<Long, Integer> getLastRestTime() {
		return lastRestTime;
	}

	public void setLastRestTime(Map<Long, Integer> lastRestTime) {
		this.lastRestTime = lastRestTime;
	}
	
}
