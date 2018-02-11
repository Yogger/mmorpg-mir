package com.mmorpg.mir.model.commonactivity.packet;

import java.util.LinkedList;

import com.mmorpg.mir.model.commonactivity.model.ServerGoldTreasuryLog;

public class SM_Gold_Treasury_Query {
	private String activeName;
	
	private LinkedList<ServerGoldTreasuryLog> logs;

	public static SM_Gold_Treasury_Query valueOf(String activeName, LinkedList<ServerGoldTreasuryLog> logs){
		SM_Gold_Treasury_Query sm = new SM_Gold_Treasury_Query();
		sm.activeName = activeName;
		sm.logs = logs;
		return sm;
	}
	
	public LinkedList<ServerGoldTreasuryLog> getLogs() {
		return logs;
	}

	public void setLogs(LinkedList<ServerGoldTreasuryLog> logs) {
		this.logs = logs;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
}	
