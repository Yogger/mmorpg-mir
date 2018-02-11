package com.mmorpg.mir.model.operator.packet;

import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeServer;

public class SM_QiHu360_Privilege_Change {
	private long startTime;
	private long endTime;

	public static SM_QiHu360_Privilege_Change valueOf(QiHu360PrivilegeServer qiHu360PrivilegeServer) {
		SM_QiHu360_Privilege_Change sm = new SM_QiHu360_Privilege_Change();
		sm.startTime = qiHu360PrivilegeServer.getstartTime().getTime();
		sm.endTime = qiHu360PrivilegeServer.getEndTime().getTime();
		return sm;
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
