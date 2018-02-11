package com.mmorpg.mir.model.world.packet;

public class SM_Remove {
	private long objId;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public static SM_Remove valueOf(long objId) {
		SM_Remove result = new SM_Remove();
		result.setObjId(objId);
		return result;
	}
}
