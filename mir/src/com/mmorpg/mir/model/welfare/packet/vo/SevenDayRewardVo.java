package com.mmorpg.mir.model.welfare.packet.vo;

import java.util.Map;

import com.mmorpg.mir.model.welfare.model.SevenDayReward;

/**
 * 七天登录奖励记录
 * 
 * @author 37.com
 * 
 */
public class SevenDayRewardVo {
	private Map<Integer, Boolean> sevendayRewardRecord;

	private int dayIndex;

	public static SevenDayRewardVo valueOf(SevenDayReward sevenDayReward) {
		SevenDayRewardVo result = new SevenDayRewardVo();
		result.sevendayRewardRecord = sevenDayReward.getSevendayDrawRecord();
		result.dayIndex = sevenDayReward.getDayIndex();
		return result;
	}

	public Map<Integer, Boolean> getSevendayRewardRecord() {
		return sevendayRewardRecord;
	}

	public void setSevendayRewardRecord(Map<Integer, Boolean> sevendayRewardRecord) {
		this.sevendayRewardRecord = sevendayRewardRecord;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

}
