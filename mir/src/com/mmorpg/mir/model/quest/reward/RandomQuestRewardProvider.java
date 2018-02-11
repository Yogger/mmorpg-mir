package com.mmorpg.mir.model.quest.reward;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestUpdate;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class RandomQuestRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.RANDOM_QUEST;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		String questId = rewardItem.getCode();
		QuestUpdate update = QuestUpdate.valueOf();
		List<String> questList = Arrays.asList(questId);
		List<Quest> newQuests = player.getQuestPool().acceptQuests(update, player, questList,
				System.currentTimeMillis(), false, true);
		if (!newQuests.isEmpty()) {
			player.getQuestPool().refreshQuest(newQuests);
		}
	}

}
