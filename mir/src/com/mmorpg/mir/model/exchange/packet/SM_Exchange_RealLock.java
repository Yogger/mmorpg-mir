package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_RealLock {
	private byte type;
	private boolean lock;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public static SM_Exchange_RealLock valueOf(byte type, boolean lock) {
		SM_Exchange_RealLock req = new SM_Exchange_RealLock();
		req.type = type;
		req.lock = lock;
		return req;
	}
}
