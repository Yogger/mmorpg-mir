package com.mmorpg.mir.model.vip.packet;

import com.mmorpg.mir.model.vip.model.Vip;

public class SM_Vip_Update {
	private int vipLevel;
	private int growthValue;
	private int add;
	private long tempVipEndTime;

	public static SM_Vip_Update valueOf(Vip vip, int add) {
		SM_Vip_Update sm = new SM_Vip_Update();
		sm.growthValue = vip.getGrowthValue();
		sm.vipLevel = vip.getLevel();
		sm.add = add;
		sm.tempVipEndTime = vip.getTempVipEndTime();
		return sm;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public long getTempVipEndTime() {
		return tempVipEndTime;
	}

	public void setTempVipEndTime(long tempVipEndTime) {
		this.tempVipEndTime = tempVipEndTime;
	}

}
