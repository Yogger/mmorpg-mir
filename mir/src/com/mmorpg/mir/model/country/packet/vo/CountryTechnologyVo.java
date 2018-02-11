package com.mmorpg.mir.model.country.packet.vo;

import com.mmorpg.mir.model.country.model.NewTechnology;

public class CountryTechnologyVo {
	private int buildValue;
	/** 等级 */
	private int grade;
	/** 军旗数量 */
	private int flagCount;
	/** 军旗数量最后刷新时间 */
	private long flagCountCDTime;
	/** 最近放置军旗的玩家名字 */
	private String lastPlaceFlagPlayerName;

	public static CountryTechnologyVo valueOf(NewTechnology technology) {
		CountryTechnologyVo result = new CountryTechnologyVo();
		result.buildValue = technology.getBuildValue();
		result.grade = technology.getGrade();
		result.flagCount = technology.getFlagCount();
		result.flagCountCDTime = technology.getNextFlagCDTime();
		result.lastPlaceFlagPlayerName = technology.getLastPlaceFlagPlayerName();
		return result;
	}

	public int getBuildValue() {
		return buildValue;
	}

	public void setBuildValue(int buildValue) {
		this.buildValue = buildValue;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
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

	public String getLastPlaceFlagPlayerName() {
		return lastPlaceFlagPlayerName;
	}

	public void setLastPlaceFlagPlayerName(String lastPlaceFlagPlayerName) {
		this.lastPlaceFlagPlayerName = lastPlaceFlagPlayerName;
	}

}
