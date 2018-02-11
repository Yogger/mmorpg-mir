package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;

public class SGM_Identify_Treasure_Query {
	private String activeName;
	
	private long startTime;
	
	private long endTime;
	
	private int code;

	public static SGM_Identify_Treasure_Query valueOf(String activeName, CommonIdentifyTreasureServer treasureServer){
		SGM_Identify_Treasure_Query sgm = new SGM_Identify_Treasure_Query();
		sgm.activeName = activeName;
		sgm.startTime = treasureServer.getStartTime();
		sgm.endTime = treasureServer.getEndTime();
		return sgm;
	}
	
	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
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
