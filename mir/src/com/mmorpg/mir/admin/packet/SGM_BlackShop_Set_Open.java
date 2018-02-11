package com.mmorpg.mir.admin.packet;

public class SGM_BlackShop_Set_Open {

	private String goodChooserId;

	private long beginTime;

	private long endTime;

	private int code;

	public static SGM_BlackShop_Set_Open valueOf(String goodChooserId, long beginTime, long endTime) {
		SGM_BlackShop_Set_Open result = new SGM_BlackShop_Set_Open();
		result.goodChooserId = goodChooserId;
		result.beginTime = beginTime;
		result.endTime = endTime;
		return result;
	}

	public String getGoodChooserId() {
		return goodChooserId;
	}

	public void setGoodChooserId(String goodChooserId) {
		this.goodChooserId = goodChooserId;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
