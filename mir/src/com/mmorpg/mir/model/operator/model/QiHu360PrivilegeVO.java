package com.mmorpg.mir.model.operator.model;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;

public class QiHu360PrivilegeVO {
	private int version;
	private ArrayList<QiHu360PrivilegeLog> logs = new ArrayList<QiHu360PrivilegeLog>();
	private long startTime;
	private long endTime;
	
	public static QiHu360PrivilegeVO valueOf(QiHu360PrivilegeServer qihuServer, Player player){
		QiHu360PrivilegeVO qihu = new QiHu360PrivilegeVO();
		if(qihuServer.getVersion() != player.getOperatorPool().getQiHuPrivilege().getVersion()){
			player.getOperatorPool().getQiHuPrivilege().reset(qihuServer);
		}
		qihu.version = qihuServer.getVersion();
		qihu.logs = player.getOperatorPool().getQiHuPrivilege().getLogs();
		if(qihuServer.getstartTime() == null){
			qihu.startTime = 0;
		}else{
			qihu.startTime = qihuServer.getstartTime().getTime();
		}
		if(qihuServer.getEndTime() == null){
			qihu.endTime = 0;
		}else{
			qihu.endTime = qihuServer.getEndTime().getTime();
		}
		return qihu;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public ArrayList<QiHu360PrivilegeLog> getLogs() {
		return logs;
	}
	public void setLogs(ArrayList<QiHu360PrivilegeLog> logs) {
		this.logs = logs;
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
