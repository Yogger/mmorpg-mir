package com.mmorpg.mir.model.player.packet;

public class SM_Player_Common {
	private int code;

	public SM_Player_Common(int code) {
		this.code = code;
	}

	public SM_Player_Common() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
