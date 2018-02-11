package com.mmorpg.mir.model.operator.packet;

import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegeServer;


public class SM_QiHu360_Speed_Privilege_Change {
	private long startTime;
	
	private long endTime;
	
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
	
	public static SM_QiHu360_Speed_Privilege_Change valueOf(QiHu360SpeedPrivilegeServer qiHu360PrivilegeServer) {
		SM_QiHu360_Speed_Privilege_Change sm = new SM_QiHu360_Speed_Privilege_Change();
		sm.setStartTime(qiHu360PrivilegeServer.getStartTime().getTime());
		sm.setEndTime(qiHu360PrivilegeServer.getEndTime().getTime());
		return sm;
	}
}
