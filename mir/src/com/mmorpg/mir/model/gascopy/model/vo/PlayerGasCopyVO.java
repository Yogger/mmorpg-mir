package com.mmorpg.mir.model.gascopy.model.vo;

import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerGasCopyVO {
	/** 今天进入的次数 **/
	private int dailyEnterCount;
	/** 瘴气值 **/
	private int gasValue;
	/** 上一次加瘴气值的时间 **/
	private long lastAddGasTime;
	
	public static PlayerGasCopyVO valueOf(Player player) {
		PlayerGasCopyVO vo = new PlayerGasCopyVO();
		vo.dailyEnterCount = player.getGasCopy().getDailyEnterCount();
		vo.gasValue = player.getGasCopy().getGasValue();
		vo.lastAddGasTime = player.getGasCopy().getLastAddGasTime();
		return vo;
	}

	public int getDailyEnterCount() {
		return dailyEnterCount;
	}

	public void setDailyEnterCount(int dailyEnterCount) {
		this.dailyEnterCount = dailyEnterCount;
	}

	public int getGasValue() {
		return gasValue;
	}

	public void setGasValue(int gasValue) {
		this.gasValue = gasValue;
	}

	public long getLastAddGasTime() {
		return lastAddGasTime;
	}

	public void setLastAddGasTime(long lastAddGasTime) {
		this.lastAddGasTime = lastAddGasTime;
	}
	
}
