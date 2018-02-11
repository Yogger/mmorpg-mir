package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.blackshop.model.BlackShopServer;

public class SGM_BlackShop_Query {

	private int code;

	private String goodGroupId;

	private long beginTime;

	private long endTime;

	public static SGM_BlackShop_Query valueOf(BlackShopServer blackShopServer) {
		SGM_BlackShop_Query result = new SGM_BlackShop_Query();
		result.goodGroupId = blackShopServer.getGoodGroupId();
		result.beginTime = blackShopServer.getBeginTime();
		result.endTime = blackShopServer.getEndTime();
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

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
