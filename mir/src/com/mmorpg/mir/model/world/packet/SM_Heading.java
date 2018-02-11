package com.mmorpg.mir.model.world.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_Heading {
	private long objId;
	private byte heading;

	public static SM_Heading valueOf(Creature creature) {
		SM_Heading result = new SM_Heading();
		result.objId = creature.getObjectId();
		result.heading = creature.getHeading();
		return result;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

}
