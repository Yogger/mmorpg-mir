package com.mmorpg.mir.model.combatspirit.manager;

import java.util.List;

import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;

public interface ICombatSpiritManager {
	CombatSpiritResource getCombatSpiritResource(String key, boolean throwException);
	
	public List<Stat> getCombatSpiritStats(String id);

	public Class<?> getResourceClass();
	
	public void reload();
}
