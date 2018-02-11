package com.mmorpg.mir.model.gang.packet;

public class SM_Quit_Gang {
	private int code;
	
	private long lastQuitGangTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getLastQuitGangTime() {
		return lastQuitGangTime;
	}

	public void setLastQuitGangTime(long lastQuitGangTime) {
		this.lastQuitGangTime = lastQuitGangTime;
	}

}
