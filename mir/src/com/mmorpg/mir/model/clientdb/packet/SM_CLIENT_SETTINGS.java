package com.mmorpg.mir.model.clientdb.packet;

public class SM_CLIENT_SETTINGS {

	private byte code;
	
	public static SM_CLIENT_SETTINGS valueOf() {
		SM_CLIENT_SETTINGS sm = new SM_CLIENT_SETTINGS();
		sm.code = 1;
		return sm;
	}

	public byte getCode() {
    	return code;
    }

	public void setCode(byte code) {
    	this.code = code;
    }

}
