package com.mmorpg.mir.model.operator.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class QiHu360SpeedPrivilegeServer {
	private int version = 1;

	private Date startTime;

	private Date endTime;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@JsonIgnore
	public void refresh(Date startTime, Date endTime) {
		version++;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@JsonIgnore
	public boolean isSomeVersion(QiHu360SpeedPrivilegePlayer qiHu360SpeedPrivilegePlayer){
		return version == qiHu360SpeedPrivilegePlayer.getVersion();
	}
	
	@JsonIgnore
	public boolean isInPrivilegeTime(){
		long now = new Date().getTime();
		if (now < startTime.getTime() || now > endTime.getTime()) {
			return false;
		}
		return true;
	}
}
