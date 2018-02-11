package com.mmorpg.mir.admin.packet;

public class GM_BlackShop_Set_Open {

	private String goodGroupId;

	private long beginTime;

	private long endTime;

	public String getGoodGroupId() {
		return goodGroupId;
	}

	public void setGoodGroupId(String goodGroupId) {
		this.goodGroupId = goodGroupId;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
