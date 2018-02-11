package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;

public class SM_TechCopy_Monster_Spawn {
	
	private long nextSpawnTime;
	
	private byte heading;
	
	public static SM_TechCopy_Monster_Spawn valueOf(long time, String spawnKey) {
		SM_TechCopy_Monster_Spawn sm = new SM_TechCopy_Monster_Spawn();
		sm.nextSpawnTime = time;
		if (spawnKey != null && !spawnKey.isEmpty()) {
			Integer head = CountryCopyConfig.getInstance().MONSTER_HEADING_MAPPING.getValue().get(spawnKey);
			sm.heading = (byte) head.intValue();
		}
		return sm;
	}

	public long getNextSpawnTime() {
		return nextSpawnTime;
	}

	public void setNextSpawnTime(long nextSpawnTime) {
		this.nextSpawnTime = nextSpawnTime;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}
	
}
