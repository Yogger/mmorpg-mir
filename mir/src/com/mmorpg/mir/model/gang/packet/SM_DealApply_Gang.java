package com.mmorpg.mir.model.gang.packet;

public class SM_DealApply_Gang {
	private int code;
	private long gangId;
	private int type;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public int getType() {
    	return type;
    }

	public void setType(int type) {
    	this.type = type;
    }

}
