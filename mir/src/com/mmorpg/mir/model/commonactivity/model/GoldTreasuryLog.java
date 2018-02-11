package com.mmorpg.mir.model.commonactivity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldTreasuryLog {
	private int resetTimes;

	private Map<Integer, String> groupLog;

	public static GoldTreasuryLog valueOf() {
		GoldTreasuryLog log = new GoldTreasuryLog();
		log.groupLog = new HashMap<Integer, String>();
		return log;
	}

	public void addLog(int index, String rewardId) {
		groupLog.put(index, rewardId);
	}

	/**
	 * 下次一定要记住， Jackson是通过get方法序列化的， 通过set方法反序列化的， 所有不应该出现get方法
	 * 
	 * @return
	 */
	public List<String> rewards() {
		return new ArrayList<String>(groupLog.values());
	}

	public void reset() {
		groupLog.clear();
		resetTimes = resetTimes + 1;
	}

	public int getResetTimes() {
		return resetTimes;
	}

	public void setResetTimes(int resetTimes) {
		this.resetTimes = resetTimes;
	}

	public Map<Integer, String> getGroupLog() {
		return groupLog;
	}

	public void setGroupLog(Map<Integer, String> groupLog) {
		this.groupLog = groupLog;
	}
}
