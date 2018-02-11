package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_Stop {

	private int code;
	
	public static SM_Exchange_Stop valueOf(int code) {
		SM_Exchange_Stop req = new SM_Exchange_Stop();
		req.code = code;
		return req;
	}

	public int getCode() {
    	return code;
    }

	public void setCode(int code) {
    	this.code = code;
    }

}
