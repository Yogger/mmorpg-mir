package com.mmorpg.mir.model.commonactivity.packet;

import java.util.LinkedList;

import com.mmorpg.mir.model.commonactivity.model.IdentifyTreasureLog;

public class SM_Common_Identify_Treasure_Query {
	private String activeName;
	
	private LinkedList<IdentifyTreasureLog> treasureLogs = new LinkedList<IdentifyTreasureLog>();

	public static SM_Common_Identify_Treasure_Query valueOf(String activeName, LinkedList<IdentifyTreasureLog> treasureLogs){
		SM_Common_Identify_Treasure_Query sm = new SM_Common_Identify_Treasure_Query();
		sm.treasureLogs = treasureLogs;
		sm.activeName = activeName;
		return sm;
	}
	
	public LinkedList<IdentifyTreasureLog> getTreasureLogs() {
		return treasureLogs;
	}

	public void setTreasureLogs(LinkedList<IdentifyTreasureLog> treasureLogs) {
		this.treasureLogs = treasureLogs;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
}
