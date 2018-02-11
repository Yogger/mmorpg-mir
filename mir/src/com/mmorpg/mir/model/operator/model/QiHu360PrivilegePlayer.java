package com.mmorpg.mir.model.operator.model;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.DateUtils;

public class QiHu360PrivilegePlayer {
	private int version;
	private ArrayList<QiHu360PrivilegeLog> logs = new ArrayList<QiHu360PrivilegeLog>();

	public static QiHu360PrivilegePlayer valueOf(){
		QiHu360PrivilegePlayer qihuServer = new QiHu360PrivilegePlayer();
		qihuServer.logs = new ArrayList<QiHu360PrivilegeLog>();
		return qihuServer;
	}
	@JsonIgnore
	public boolean isTodayRewarded(String id) {
		for (QiHu360PrivilegeLog log : logs) {
			if (log.getId().equals(id) && DateUtils.isToday(new Date(log.getTime()))) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public void reset(QiHu360PrivilegeServer qiHu360PrivilegeServer) {
		this.version = qiHu360PrivilegeServer.getVersion();
		this.logs.clear();
	}

	public ArrayList<QiHu360PrivilegeLog> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<QiHu360PrivilegeLog> logs) {
		this.logs = logs;
	}

	@JsonIgnore
	public boolean hasDrawBefore(String id) {
		for (QiHu360PrivilegeLog log : logs) {
			if (log.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public void addQiHu360PrivilegeLog(QiHu360PrivilegeLog log) {
		logs.add(log);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
