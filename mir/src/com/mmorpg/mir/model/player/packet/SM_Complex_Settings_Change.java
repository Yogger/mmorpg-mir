package com.mmorpg.mir.model.player.packet;

public class SM_Complex_Settings_Change {

	private long playerId;
	private byte[] complexState;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public byte[] getComplexState() {
		return complexState;
	}

	public void setComplexState(byte[] complexState) {
		this.complexState = complexState;
	}

	public static SM_Complex_Settings_Change valueOf(long pid, byte[] states) { 
		SM_Complex_Settings_Change sm = new SM_Complex_Settings_Change();
		sm.playerId = pid;
		sm.complexState = states;
		return sm;
	}
}
