package com.mmorpg.mir.model.operator.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class QiHu360SpeedPrivilegeVO {
	private long startTime;
	
	private long endTime;
	
	private boolean hasDrawToDay;
	
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
	
	public boolean isHasDrawToDay() {
		return hasDrawToDay;
	}

	public void setHasDrawToDay(boolean hasDrawToDay) {
		this.hasDrawToDay = hasDrawToDay;
	}
	
	public static QiHu360SpeedPrivilegeVO valueOf(QiHu360SpeedPrivilegeServer server, Player player){
		QiHu360SpeedPrivilegeVO vo = new QiHu360SpeedPrivilegeVO();
		vo.startTime = server.getStartTime().getTime();
		vo.endTime = server.getEndTime().getTime();
		vo.hasDrawToDay = player.getOperatorPool().getQiHuSpeedPrivilege().hasDrawToDay();
		return vo;
	}
}
