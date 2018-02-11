package com.mmorpg.mir.model.player.packet;

public class SM_CheckNameExist {

	private byte result;

	public static SM_CheckNameExist valueOf(byte result) {
		SM_CheckNameExist sm = new SM_CheckNameExist();
		sm.result = result;
		return sm;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

}
