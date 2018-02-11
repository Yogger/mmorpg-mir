package com.mmorpg.mir.model.fashion.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_FashionBroadcast {
	private long objId;

	private int currentFashionId;

	public static SM_FashionBroadcast valueOf(Player player) {
		SM_FashionBroadcast result = new SM_FashionBroadcast();
		result.objId = player.getObjectId();
		result.currentFashionId = player.getFashionPool().isHided() ? -1 : player.getFashionPool()
				.getCurrentFashionId();
		return result;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public int getCurrentFashionId() {
		return currentFashionId;
	}

	public void setCurrentFashionId(int currentFashionId) {
		this.currentFashionId = currentFashionId;
	}

}
