package com.mmorpg.mir.model.item.packet;

public class CM_Equip_Create_Soul {

	private long objId;
	
	private String key;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
