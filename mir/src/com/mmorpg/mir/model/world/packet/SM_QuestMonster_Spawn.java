package com.mmorpg.mir.model.world.packet;

public class SM_QuestMonster_Spawn {
	private long objId;
	private String key;
	private byte spawn;

	public static SM_QuestMonster_Spawn valueOf(long objId, String key, byte spawn) {
		SM_QuestMonster_Spawn sm = new SM_QuestMonster_Spawn();
		sm.objId = objId;
		sm.key = key;
		sm.spawn = spawn;
		return sm;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte getSpawn() {
		return spawn;
	}

	public void setSpawn(byte spawn) {
		this.spawn = spawn;
	}
}
