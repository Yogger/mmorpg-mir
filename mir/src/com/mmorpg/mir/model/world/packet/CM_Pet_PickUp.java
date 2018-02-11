package com.mmorpg.mir.model.world.packet;

import java.util.HashSet;

public class CM_Pet_PickUp {
	
	private HashSet<Long> objIds;

	public HashSet<Long> getObjIds() {
		return objIds;
	}

	public void setObjIds(HashSet<Long> objIds) {
		this.objIds = objIds;
	}
	
}
