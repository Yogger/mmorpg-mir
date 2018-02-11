package com.mmorpg.mir.model.country.model.vo;

import java.util.Date;

import com.windforce.common.utility.DateUtils;

public class ActivityVO {

	private long startTime;
	private long endTime;

	public static ActivityVO valueOf(long startHourMillis, long endHourMillis) {
		ActivityVO vo = new ActivityVO();
		vo.setStartTime(startHourMillis);
		vo.setEndTime(endHourMillis);
		return vo;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "startTime :" + DateUtils.date2String(new Date(startTime), "yyyy-MM-dd HH:mm:ss") + " | endTime = "
				+ DateUtils.date2String(new Date(endTime), "yyyy-MM-dd HH:mm:ss");
	}

}
