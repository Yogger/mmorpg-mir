package com.mmorpg.mir.model.combatspirit.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class CombatSpiritManager implements ICombatSpiritManager, ResourceReload{

	@Static
	private Storage<String, CombatSpiritResource> combatSpiritResources;
	

	@Static("EQIUPMENT:COMBAT_SPIRIT_INIT")
	public ConfigValue<Map<String, String>> COMBAT_SPIRIT_INIT;
	
	@Static("EQIUPMENT:COMBAT_SPIRIT_OPID")
	public ConfigValue<Map<String, String>> COMBAT_SPIRIT_OPID;
	
	@Static("EQIUPMENT:COMBAT_SPIRIT_ID_INIT")
	public ConfigValue<Map<String, String>> COMBAT_SPIRIT_ID_INIT;
	
	@Static("EQUIPMENT:TREASOURE_MODULE_OPEN")
	public ConfigValue<String> COMBATSPIRIT_MODULE_OPEN;

	@Static("EQUIPMENT:KILL_BOSS_ADD")
	public ConfigValue<Integer> KILL_BOSS_ADD;
	
	@Static("EQUIPMENT:KILL_MONSTER_ADD")
	public ConfigValue<Integer> KILL_MONSTER_ADD;
	
	@Static("EQUIPMENT:MEDAL_ADD_LEVEL_LIMIT")
	public ConfigValue<Integer> MEDAL_ADD_LEVEL_LIMIT;

	private static CombatSpiritManager instance;
	
	private Map<String, List<Stat>> combatSpiritStatMap; 
	
	@PostConstruct
	void init() {
		instance = this;
		reload();
	}
	
	public static CombatSpiritManager getInstance() {
		return instance;
	}
	
	public CombatSpiritResource getCombatSpiritResource(String key, boolean throwException) {
		return combatSpiritResources.get(key, throwException);
	}

	@Override
	public void reload() {
		Map<String, List<Stat>> map = New.hashMap();
		for (CombatSpiritResource resource: combatSpiritResources.getAll()) {
			List<Stat> stats = New.arrayList();
			if (resource.getCurrentStats() != null) {
				for (Stat s: resource.getCurrentStats()) {
					stats.add(s);
				}
			}
			if (resource.getCurrentQualityStats() != null) {
				for (Stat s: resource.getCurrentQualityStats()) {
					stats.add(s);
				}
			}
			map.put(resource.getId(), stats);
		}
		
		combatSpiritStatMap = map;
	}
	
	public List<Stat> getCombatSpiritStats(String id) {
		return combatSpiritStatMap.get(id);
	}
	
	@Override
	public Class<?> getResourceClass() {
		return CombatSpiritManager.class;
	}
}
