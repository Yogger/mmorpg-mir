package com.mmorpg.mir.model.gameobjects.event;

import com.windforce.common.event.event.IEvent;

public class MonsterKillEvent implements IEvent {

	private long owner;

	private String key;
	
	private String spawnKey;

	private boolean useForMedal;
	
	private int level;
	
	private long mostDamagePlayer;
	
	private boolean knowPlayer;
	
	public static MonsterKillEvent valueOf(long owner, String key, boolean medal, int level, String spawnKey, long mostDamagePlayer, boolean knowPlayer) {
		MonsterKillEvent mke = new MonsterKillEvent();
		mke.owner = owner;
		mke.setKey(key);
		mke.useForMedal = medal;
		mke.level = level;
		mke.spawnKey = spawnKey;
		mke.mostDamagePlayer = mostDamagePlayer;
		mke.knowPlayer = knowPlayer;
		return mke;
	}
	
	public static MonsterKillEvent valueOf(long owner, String key) {
		MonsterKillEvent mke = new MonsterKillEvent();
		mke.owner = owner;
		mke.setKey(key);
		return mke;
	}

	@Override
	public long getOwner() {
		return this.owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isUseForMedal() {
    	return useForMedal;
    }

	public void setUseForMedal(boolean useForMedal) {
    	this.useForMedal = useForMedal;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public long getMostDamagePlayer() {
		return mostDamagePlayer;
	}

	public void setMostDamagePlayer(long mostDamagePlayer) {
		this.mostDamagePlayer = mostDamagePlayer;
	}

	public boolean isKnowPlayer() {
		return knowPlayer;
	}

	public void setKnowPlayer(boolean knowPlayer) {
		this.knowPlayer = knowPlayer;
	}

}
