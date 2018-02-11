package com.mmorpg.mir.model.world.packet;

import java.util.HashSet;

public class SM_Pet_PickUp {

	private int code;
	
	private HashSet<Long> objIds;
	
	public static SM_Pet_PickUp valueOf(HashSet<Long> objs) {
		SM_Pet_PickUp sm = new SM_Pet_PickUp();
		sm.objIds = objs;
		return sm;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HashSet<Long> getObjIds() {
		return objIds;
	}

	public void setObjIds(HashSet<Long> objIds) {
		this.objIds = objIds;
	}
	
}
