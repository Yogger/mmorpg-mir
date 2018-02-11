package com.mmorpg.mir.model.footprint.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Change_Footprint {
	private long objectId;
	private int id;
	private byte type;

	public static SM_Change_Footprint valueOf(Player player, byte type) {
		SM_Change_Footprint sm = new SM_Change_Footprint();
		sm.objectId = player.getObjectId();
		sm.id = player.getFootprintPool().getOpenFootprint();
		sm.type = type;
		return sm;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
