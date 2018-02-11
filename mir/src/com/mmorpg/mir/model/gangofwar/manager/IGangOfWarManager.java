package com.mmorpg.mir.model.gangofwar.manager;

import java.util.Map;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;

public interface IGangOfWarManager {
	void init();

	void leaveWar(Player player);

	ActionObserver gangOfWarSpawnObsever(final Player player);

	void backhome(Player player);

	void enterWar(Player player);

	void start(int countryId, boolean mergeFirst);

	Map<CountryId, GangOfWar> getGangOfwars();

	GangOfWar getGangOfWars(Player player);
}
