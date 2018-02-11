package com.mmorpg.mir.model.commonactivity.packet;

import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;

public class SM_Common_Identify_Treasure_Change {
	private String activeName;
	
	private long startTime;
	
	private long endTime;

	public static SM_Common_Identify_Treasure_Change valueOf( String activeName, CommonIdentifyTreasureServer treasureServer){
		SM_Common_Identify_Treasure_Change sm = new SM_Common_Identify_Treasure_Change();
		sm.activeName = activeName;
		sm.startTime = treasureServer.getStartTime();
		sm.endTime = treasureServer.getEndTime();
		return sm;
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
}
