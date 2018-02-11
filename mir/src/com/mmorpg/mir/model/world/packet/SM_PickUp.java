package com.mmorpg.mir.model.world.packet;

public class SM_PickUp {
	private int code;
	private long objId;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
