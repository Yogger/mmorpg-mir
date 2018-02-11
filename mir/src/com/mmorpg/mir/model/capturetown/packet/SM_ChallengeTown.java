package com.mmorpg.mir.model.capturetown.packet;

public class SM_ChallengeTown {

	private int code;
	
	private long nextChanllengeTime;
	
	private int type;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getNextChanllengeTime() {
		return nextChanllengeTime;
	}

	public void setNextChanllengeTime(long nextChanllengeTime) {
		this.nextChanllengeTime = nextChanllengeTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
