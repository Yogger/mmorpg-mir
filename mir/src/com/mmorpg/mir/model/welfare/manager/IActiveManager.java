package com.mmorpg.mir.model.welfare.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ActiveEnum;
import com.mmorpg.mir.model.welfare.resource.ActiveResource;
import com.mmorpg.mir.model.welfare.resource.ActiveRewardResource;

public interface IActiveManager {
	ActiveResource getActiveResource(int eventId);

	ActiveRewardResource getActiveRewardResource(int activeValue);

	void exec(Player player, ActiveEnum activeEnum, int value);

	void exec(Player player, ActiveEnum activeEnum);

	void check(Player player);

	void moduleOpen(Player player);
}
