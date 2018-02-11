package com.mmorpg.mir.model.welfare.packet;

public class CM_Get_Red_Gift {
	private String spawnKey;
	private long deadTime;

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

}
