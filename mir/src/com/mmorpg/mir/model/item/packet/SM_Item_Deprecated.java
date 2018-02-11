package com.mmorpg.mir.model.item.packet;

public class SM_Item_Deprecated {
	private int type;
	private long objId;
	
	public static SM_Item_Deprecated valueOf(int type, long objId) {
		SM_Item_Deprecated sm = new SM_Item_Deprecated();
		sm.type = type;
		sm.objId = objId;
		return sm;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}
	
}
