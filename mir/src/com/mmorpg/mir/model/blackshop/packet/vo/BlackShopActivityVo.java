package com.mmorpg.mir.model.blackshop.packet.vo;

import com.mmorpg.mir.model.blackshop.model.BlackShopServer;
import com.mmorpg.mir.model.serverstate.ServerState;

public class BlackShopActivityVo {

	private long beginTime;

	private long endTime;

	public static BlackShopActivityVo valueOf() {
		BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
		BlackShopActivityVo result = new BlackShopActivityVo();
		result.beginTime = blackShopServer.getBeginTime();
		result.endTime = blackShopServer.getEndTime();
		return result;
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
