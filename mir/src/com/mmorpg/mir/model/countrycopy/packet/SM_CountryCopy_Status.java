package com.mmorpg.mir.model.countrycopy.packet;

public class SM_CountryCopy_Status {
	/** 助威的人数 **/
	private int encourageNums;
	/** BOSS当前血量 **/
	private long currentHp;
	/** BOSS血量上限 **/
	private long maxHp;

	public static SM_CountryCopy_Status valueOf(int nums, long hp, long maxHp) {
		SM_CountryCopy_Status sm = new SM_CountryCopy_Status();
		sm.encourageNums = nums;
		sm.currentHp = hp;
		sm.maxHp = maxHp;
		return sm;
	}

	public int getEncourageNums() {
		return encourageNums;
	}

	public void setEncourageNums(int encourageNums) {
		this.encourageNums = encourageNums;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

}
