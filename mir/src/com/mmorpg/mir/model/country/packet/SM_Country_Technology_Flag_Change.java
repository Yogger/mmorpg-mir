package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.NewTechnology;

public class SM_Country_Technology_Flag_Change {
	private String lastPlaceFlagPlayerName;

	private int flagCount;

	private long flagCountCDTime;

	public static SM_Country_Technology_Flag_Change valueOf(NewTechnology technology) {
		SM_Country_Technology_Flag_Change result = new SM_Country_Technology_Flag_Change();
		result.lastPlaceFlagPlayerName = technology.getLastPlaceFlagPlayerName();
		result.flagCount = technology.getFlagCount();
		result.flagCountCDTime = technology.getNextFlagCDTime();
		return result;
	}

	public String getLastPlaceFlagPlayerName() {
		return lastPlaceFlagPlayerName;
	}

	public void setLastPlaceFlagPlayerName(String lastPlaceFlagPlayerName) {
		this.lastPlaceFlagPlayerName = lastPlaceFlagPlayerName;
	}

	public int getFlagCount() {
		return flagCount;
	}

	public void setFlagCount(int flagCount) {
		this.flagCount = flagCount;
	}

	public long getFlagCountCDTime() {
		return flagCountCDTime;
	}

	public void setFlagCountCDTime(long flagCountCDTime) {
		this.flagCountCDTime = flagCountCDTime;
	}

}
