package com.mmorpg.mir.model.operator.model;

import java.util.Date;
import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.DateUtils;

public class QiHu360SpeedPrivilegePlayer {
	private int version;
	
	private LinkedList<Long>  logs = new LinkedList<Long>();
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public LinkedList<Long> getLogs() {
		return logs;
	}
	
	public void setLogs(LinkedList<Long> logs) {
		this.logs = logs;
	}
	
	public static QiHu360SpeedPrivilegePlayer valueOf(){
		QiHu360SpeedPrivilegePlayer qihu = new QiHu360SpeedPrivilegePlayer();
		qihu.logs = new LinkedList<Long>();
		return qihu;
	}
	
	@JsonIgnore
	public void reset(QiHu360SpeedPrivilegeServer server){
		this.version = server.getVersion();
		this.logs.clear();
	}
	@JsonIgnore
	public boolean hasDrawToDay(){
		if(logs.isEmpty()){
			return false;
		}
		Long lastDraw = logs.getLast();
		if(DateUtils.isToday(new Date(lastDraw.longValue()))){
			return true;
		}
		return false;
	}
	@JsonIgnore
	public void addDrawLog(Long log){
		logs.add(log);
	}
}
