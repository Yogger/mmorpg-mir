package com.mmorpg.mir.model.country.packet;

public class SM_CountryTechnology_BuildValue_Change {

	private int grade;

	private int buildValueAdd;

	private int totalBuildValue;

	// 是否是升级推送
	private boolean upgradBroadcast;

	public static SM_CountryTechnology_BuildValue_Change valueOf(int grade, int buildValueAdd, int totalBuildValue,
			boolean upgradBroadcast) {
		SM_CountryTechnology_BuildValue_Change result = new SM_CountryTechnology_BuildValue_Change();
		result.grade = grade;
		result.buildValueAdd = buildValueAdd;
		result.totalBuildValue = totalBuildValue;
		result.upgradBroadcast = upgradBroadcast;
		return result;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getTotalBuildValue() {
		return totalBuildValue;
	}

	public void setTotalBuildValue(int totalBuildValue) {
		this.totalBuildValue = totalBuildValue;
	}

	public int getBuildValueAdd() {
		return buildValueAdd;
	}

	public void setBuildValueAdd(int buildValueAdd) {
		this.buildValueAdd = buildValueAdd;
	}

	public boolean isUpgradBroadcast() {
		return upgradBroadcast;
	}

	public void setUpgradBroadcast(boolean upgradBroadcast) {
		this.upgradBroadcast = upgradBroadcast;
	}

}
