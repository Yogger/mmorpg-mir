package com.mmorpg.mir.model.item.packet;

public class CM_EquipReElement {

	private long objId;
	private boolean isGold;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public boolean isGold() {
		return isGold;
	}

	public void setGold(boolean isGold) {
		this.isGold = isGold;
	}
	
}
