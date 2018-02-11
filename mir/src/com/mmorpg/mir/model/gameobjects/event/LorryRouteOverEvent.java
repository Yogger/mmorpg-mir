package com.mmorpg.mir.model.gameobjects.event;

import com.windforce.common.event.event.IEvent;

public class LorryRouteOverEvent implements IEvent {

	private long owner;

	private String questId;

	public static LorryRouteOverEvent valueOf(long owner, String questId) {
		LorryRouteOverEvent mke = new LorryRouteOverEvent();
		mke.owner = owner;
		mke.setQuestId(questId);
		return mke;
	}

	@Override
	public long getOwner() {
		return this.owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String key) {
		this.questId = key;
	}

}
