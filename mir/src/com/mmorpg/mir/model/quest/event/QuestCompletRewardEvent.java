package com.mmorpg.mir.model.quest.event;

import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.windforce.common.event.event.IEvent;

public class QuestCompletRewardEvent implements IEvent {
	private long owner;
	private Quest quest;
	private QuestType type;
	private String rewardChooserId;

	public static QuestCompletRewardEvent valueOf(long owner, Quest quest, QuestType type, String rewardChooserId) {
		QuestCompletRewardEvent event = new QuestCompletRewardEvent();
		event.owner = owner;
		event.quest = quest;
		event.type = type;
		event.rewardChooserId = rewardChooserId;
		return event;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public QuestType getType() {
		return type;
	}

	public String getRewardChooserId() {
		return rewardChooserId;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

}
