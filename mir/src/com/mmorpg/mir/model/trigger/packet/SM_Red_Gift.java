package com.mmorpg.mir.model.trigger.packet;

public class SM_Red_Gift {

	private long deadTime;
	private String spawnKey;

	public static SM_Red_Gift valueOf(long time, String key) {
		SM_Red_Gift sm = new SM_Red_Gift();
		sm.deadTime = time;
		sm.spawnKey = key;
		return sm;
	}
	
	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

}
