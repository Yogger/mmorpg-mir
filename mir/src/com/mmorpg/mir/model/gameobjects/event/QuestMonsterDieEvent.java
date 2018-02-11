package com.mmorpg.mir.model.gameobjects.event;

import com.windforce.common.event.event.IEvent;

public class QuestMonsterDieEvent implements IEvent {

	private long owner;

	private String questId;

	private String key;

	public static QuestMonsterDieEvent valueOf(long owner, String questId, String key) {
		QuestMonsterDieEvent mke = new QuestMonsterDieEvent();
		mke.owner = owner;
		mke.setQuestId(questId);
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

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String key) {
		this.questId = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
