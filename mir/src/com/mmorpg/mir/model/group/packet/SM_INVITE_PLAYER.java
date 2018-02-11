package com.mmorpg.mir.model.group.packet;

@Deprecated
public class SM_INVITE_PLAYER {
	private int code;

	public SM_INVITE_PLAYER(int code) {
		this.code = code;
	}

	public SM_INVITE_PLAYER() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
