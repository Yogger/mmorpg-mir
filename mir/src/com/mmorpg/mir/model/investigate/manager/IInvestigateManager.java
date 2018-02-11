package com.mmorpg.mir.model.investigate.manager;

import com.mmorpg.mir.model.gameobjects.Player;

public interface IInvestigateManager {
	void init();

	void accept(Player player);

	void change(Player player, boolean useGold);

	void takeInfo(Player player);

	void complete(Player player);

	void queryInvestigateStatus(Player player);
}
