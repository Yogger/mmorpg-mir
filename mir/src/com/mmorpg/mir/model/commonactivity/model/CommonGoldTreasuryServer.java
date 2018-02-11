package com.mmorpg.mir.model.commonactivity.model;

import java.util.LinkedList;

public class CommonGoldTreasuryServer {
	private LinkedList<ServerGoldTreasuryLog> logs;
	
	private ServerGoldTreasuryLog latestLog;

	public static CommonGoldTreasuryServer valueOf() {
		CommonGoldTreasuryServer server = new CommonGoldTreasuryServer();
		server.logs = new LinkedList<ServerGoldTreasuryLog>();
		return server;
	}

	public synchronized void addLog(String playerName, String itemId, int groupId) {
		if (logs.size() >= 48) {
			logs.removeFirst();
		}
		logs.add(ServerGoldTreasuryLog.valueOf(playerName, itemId, groupId));
	}

	public LinkedList<ServerGoldTreasuryLog> getLogs() {
		return logs;
	}

	public void setLogs(LinkedList<ServerGoldTreasuryLog> logs) {
		this.logs = logs;
	}

	public ServerGoldTreasuryLog getLatestLog() {
		return latestLog;
	}

	public void setLatestLog(ServerGoldTreasuryLog latestLog) {
		this.latestLog = latestLog;
	}
}
