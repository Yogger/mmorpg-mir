package com.mmorpg.mir.model.welfare.packet;

/** 补签 */
public class CM_Welfare_Sign_Fill_Sign {
	
	private int reqTimeSeconds; // 补签那天的时间秒

	public int getReqTimeSeconds() {
    	return reqTimeSeconds;
    }

	public void setReqTimeSeconds(int reqTimeSeconds) {
    	this.reqTimeSeconds = reqTimeSeconds;
    }

}