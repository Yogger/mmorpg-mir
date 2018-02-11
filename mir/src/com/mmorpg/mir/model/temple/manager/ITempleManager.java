package com.mmorpg.mir.model.temple.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.temple.packet.SM_Temple_StartTake_Brick;

public interface ITempleManager {
	void acceptQuest(Player player);

	void changeBrick(Player player, boolean useGold);

	SM_Temple_StartTake_Brick startBrick(final Player player, int country);

	void endTakeBrick(final Player player, int country);

	void putBrick(final Player player);

	void queryTempleStatus(Player player);
}
