package com.mmorpg.mir.model.quest.event;

import com.windforce.common.event.event.IEvent;

public class QuestCompletEvent implements IEvent {
	private long owner;
	private String questId;
	private int count;

	public static QuestCompletEvent valueOf(long owner, String questId, int count) {
		QuestCompletEvent event = new QuestCompletEvent();
		event.owner = owner;
		event.questId = questId;
		event.count = count;
		return event;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
