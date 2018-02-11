package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_Die {
	private long objId;
	private int skillId;
	private long lastId;

	public static SM_Die valueOf(Creature creature, int skillId, long lastId) {
		SM_Die result = new SM_Die();
		result.objId = creature.getObjectId();
		result.skillId = skillId;
		result.lastId = lastId;
		return result;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

}
