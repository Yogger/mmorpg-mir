package com.mmorpg.mir.admin.packet;

public class SGM_Identify_Treasure_Open {
	private String activeName;
	
	private long startTime;
	
	private long endTime;
	
	private int code;
	
	public static SGM_Identify_Treasure_Open valueOf(GM_Identify_Treasure_Open gm){
		SGM_Identify_Treasure_Open sgm = new SGM_Identify_Treasure_Open();
		sgm.activeName = gm.getActiveName();
		sgm.startTime = gm.getStartTime();
		sgm.endTime = gm.getEndTime();
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
