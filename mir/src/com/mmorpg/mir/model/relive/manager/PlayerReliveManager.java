package com.mmorpg.mir.model.relive.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class PlayerReliveManager implements IPlayerReliveManager {

	@Static("PUBLIC:AUTO_RELIVE_TIME")
	public ConfigValue<Integer> AUTO_RELIVE_TIME;
	
	@Static("PERSON:RELIVE_CLEAR_DEAD_COUNT_TIME")
	public ConfigValue<Integer> RELIVE_CLEAR_DEAD_COUNT_TIME;

	// @Static("PUBLIC:FREE_RELIVE_SKILLID")
	// private ConfigValue<Integer> FREE_RELIVE_SKILLID;
	
	@Static("PERSON:RELIVE_BOSS_INSPECTERS")
	public ConfigValue<String[]> RELIVE_BOSS_INSPECTERS;
	
	@Static("PERSON:RELIVE_TIME_CD")
	private Formula RELIVE_TIME_CD;
	
	private Map<Integer, List<VisibleObject>> INSPECTERMAP;
	
	public void initAll() {
		INSPECTERMAP = new HashMap<Integer, List<VisibleObject>>();
		for (String spawnKey : RELIVE_BOSS_INSPECTERS.getValue()) {
			SpawnGroupResource resource = SpawnManager.getInstance().getSpawn(spawnKey);
			VisibleObject visObj = SpawnManager.getInstance().spawnObject(resource, 1);
			if (!INSPECTERMAP.containsKey(visObj.getMapId())) {
				INSPECTERMAP.put(visObj.getMapId(), new ArrayList<VisibleObject>());
			}
			INSPECTERMAP.get(visObj.getMapId()).add(visObj);
		}
	}

	@Static
	private Storage<Integer, ReliveBaseResource> reliveBaseResources;

	private static PlayerReliveManager INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static PlayerReliveManager getInstance() {
		return INSTANCE;
	}

	public int getAutoReliveCD() {
		return AUTO_RELIVE_TIME.getValue();
	}

	public Storage<Integer, ReliveBaseResource> getReliveBaseResources() {
		return reliveBaseResources;
	}

	public void setReliveBaseResources(Storage<Integer, ReliveBaseResource> reliveBaseResources) {
		this.reliveBaseResources = reliveBaseResources;
	}

	public ReliveBaseResource getReliveResource(int reliveId) {
		return reliveBaseResources.get(reliveId, true);
	}

	public String getChooserGroupId(int reliveId) {
		return getReliveResource(reliveId).getChooserGroupId();
	}

	public boolean isInBossRange(Creature creature) {
		List<VisibleObject> visObjs = INSPECTERMAP.get(creature.getMapId());
		if (visObjs == null) {
			return false;
		}
		for (VisibleObject visObj : visObjs) {
			if (visObj.getKnownList().knowns(creature)) {
				return true;
			}
		}
		return false;
	}

	public long getClearDeadCountTime() {
		return RELIVE_CLEAR_DEAD_COUNT_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
	}
	
	public int caculateBuyLifeCost(Player player, Formula RELIFE_COST) {
		return (Integer) FormulaParmsUtil.valueOf(RELIFE_COST).addParm("n", player.getLifeStats().getOriginalBuyCount()).getValue();
	}
	
	public long caculateBuyCountCD(Player player) {
		Long time = (Long) FormulaParmsUtil.valueOf(RELIVE_TIME_CD).addParm("n", player.getLifeStats().getOriginalBuyCount()).getValue();
		return time * DateUtils.MILLIS_PER_SECOND;
	}
	
}
