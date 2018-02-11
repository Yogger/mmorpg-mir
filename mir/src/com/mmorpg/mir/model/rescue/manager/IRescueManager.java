package com.mmorpg.mir.model.rescue.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.rescue.model.RescueType;
import com.mmorpg.mir.model.rescue.model.typehandle.RescueTypeHandle;

public interface IRescueManager {
	public void init();

	public RescueTypeHandle getRescueTypeHandle(RescueType type);

	public void rewardRescue(Player player, Quest quest, String rewardChooserId);
}
