package com.mmorpg.mir.model.daily.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public interface IDailyQuestManager {
	void rewardDaily(Player player, Quest quest, String rewardChooserId);
}
