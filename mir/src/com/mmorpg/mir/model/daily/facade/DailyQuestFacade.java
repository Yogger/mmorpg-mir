package com.mmorpg.mir.model.daily.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.daily.manager.DailyQuestManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.event.QuestCompletRewardEvent;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class DailyQuestFacade {

	@Autowired
	private DailyQuestManager dailyQuestManager;

	@Autowired
	private PlayerManager playerManager;

	@ReceiverAnno
	public void dailyQuestReward(QuestCompletRewardEvent event) {
		if (event.getType() == QuestType.DAY) {
			dailyQuestManager.rewardDaily(playerManager.getPlayer(event.getOwner()), event.getQuest(),
					event.getRewardChooserId());
		}
	}
	
}
