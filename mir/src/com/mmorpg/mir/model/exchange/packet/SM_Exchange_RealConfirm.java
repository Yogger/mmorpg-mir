package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_RealConfirm {
	private byte type;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public static SM_Exchange_RealConfirm valueOf(byte type) {
		SM_Exchange_RealConfirm req = new SM_Exchange_RealConfirm();
		req.type = type;
		return req;
	}
}
