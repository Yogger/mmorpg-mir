package com.mmorpg.mir.model.country.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface HiddenMissionService {

	void protectCountryFlag(Player killer, Player killed);

	void protectDiplomacy(Player killer, Player killed);

	void robBrickMan(Player killer, Player killed);

	void robLorryByGroup(Player player);

	void robLorryByIndividual(Player player);
}
