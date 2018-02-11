package com.mmorpg.mir.model.quest.packet;

public class SM_RandomQuest_CompleteTime {
	private long randomQuestCompleteTime;

	public static SM_RandomQuest_CompleteTime valueOf(long randomQuestCompleteTime) {
		SM_RandomQuest_CompleteTime cr = new SM_RandomQuest_CompleteTime();
		cr.randomQuestCompleteTime = randomQuestCompleteTime;
		return cr;
	}

	public long getRandomQuestCompleteTime() {
		return randomQuestCompleteTime;
	}

	public void setRandomQuestCompleteTime(long randomQuestCompleteTime) {
		this.randomQuestCompleteTime = randomQuestCompleteTime;
	}

}
