package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;

public class SM_UnRideBroadcast {
	private long objId;

	public static SM_UnRideBroadcast valueOf(Player player) {
		SM_UnRideBroadcast resp = new SM_UnRideBroadcast();
		resp.objId = player.getObjectId();
		return resp;
	}

	public static SM_UnRideBroadcast valueOf(Summon summon) {
		SM_UnRideBroadcast resp = new SM_UnRideBroadcast();
		resp.objId = summon.getObjectId();
		return resp;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

}
