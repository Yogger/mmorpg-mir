package com.mmorpg.mir.model.welfare.model;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class SevenDayReward {
	/** 七天登录领奖记录 */
	private Map<Integer, Boolean> sevendayDrawRecord;

	/** 当前第几天 */
	private int dayIndex;

	private long lastAddDayIndexTime;

	public static SevenDayReward valueOf() {
		SevenDayReward result = new SevenDayReward();
		result.dayIndex = 1;
		result.sevendayDrawRecord = New.hashMap();
		result.sevendayDrawRecord.put(1, false);
		result.lastAddDayIndexTime = System.currentTimeMillis();
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void addDayIndex() {
		if (dayIndex >= 7) {
			return;
		}

		if (DateUtils.isToday(new Date(lastAddDayIndexTime))) {
			return;
		}

		lastAddDayIndexTime = System.currentTimeMillis();
		dayIndex++;
		sevendayDrawRecord.put(dayIndex, false);
	}

	@JsonIgnore
	public boolean isDayRewarded(int dayIndex) {
		return this.sevendayDrawRecord.get(dayIndex);
	}

	@JsonIgnore
	public void drawReward(int dayIndex) {
		if (!isDayRewarded(dayIndex)) {
			sevendayDrawRecord.put(dayIndex, true);
		}
	}

	public Map<Integer, Boolean> getSevendayDrawRecord() {
		return sevendayDrawRecord;
	}

	public void setSevendayDrawRecord(Map<Integer, Boolean> sevendayDrawRecord) {
		this.sevendayDrawRecord = sevendayDrawRecord;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public long getLastAddDayIndexTime() {
		return lastAddDayIndexTime;
	}

	public void setLastAddDayIndexTime(long lastAddDayIndexTime) {
		this.lastAddDayIndexTime = lastAddDayIndexTime;
	}

}
