package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_Request {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static SM_Exchange_Request valueOf() {
		SM_Exchange_Request req = new SM_Exchange_Request();
		return req;
	}
}
