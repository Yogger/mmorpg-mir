package com.mmorpg.mir.model.gameobjects.event;

import com.windforce.common.event.event.IEvent;

public class LorryDieEvent implements IEvent {

	private long owner;

	private String key;

	public static LorryDieEvent valueOf(long owner, String key) {
		LorryDieEvent mke = new LorryDieEvent();
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

}
