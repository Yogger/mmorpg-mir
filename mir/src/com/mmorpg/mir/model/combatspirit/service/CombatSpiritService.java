package com.mmorpg.mir.model.combatspirit.service;

import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;

public interface CombatSpiritService {
	CoreConditions getProctureAcquireConds();

	void upgradeCombatSpirit(Player player, CombatSpiritType combatSpiritType, boolean auto);

	void queryCombatSpirit(Player player);

	void openCombatSpirit(ModuleOpenEvent event, CombatSpiritType spiritType);

	void upgradeMedal(MonsterKillEvent event);

	void killGainBenifit(Player killer, Player killed);
}
