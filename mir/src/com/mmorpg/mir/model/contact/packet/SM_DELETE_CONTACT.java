package com.mmorpg.mir.model.contact.packet;

public class SM_DELETE_CONTACT {

	private int code;
	private long playerId;

	public static SM_DELETE_CONTACT valueOf(long pid, int code) {
		SM_DELETE_CONTACT sm = new SM_DELETE_CONTACT();
		sm.playerId = pid;
		sm.code = code;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
