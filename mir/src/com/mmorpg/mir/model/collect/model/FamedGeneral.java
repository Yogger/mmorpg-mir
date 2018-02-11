package com.mmorpg.mir.model.collect.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.collect.manager.CollectManager;
import com.mmorpg.mir.model.collect.resource.CollectGeneralResource;
import com.mmorpg.mir.model.collect.resource.CollectGeneralSkillCombiResource;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;

public class FamedGeneral {

	private Map<String, Map<String, Integer>> collectedFamedGeneral;
	
	private HashSet<String> activeCollectGeneralIds;

	public static FamedGeneral valueOf() {
		FamedGeneral fm = new FamedGeneral();
		fm.collectedFamedGeneral = new HashMap<String, Map<String, Integer>>();
		fm.activeCollectGeneralIds = new HashSet<String>();
		return fm;
	}
	
	// login stats init
	@JsonIgnore
	public void initFamedGeneralCollectStats(Player player) {
		for (CollectGeneralResource res : CollectManager.getInstance().collectGeneralResources.getAll()) {
			if (activeCollectGeneralIds.contains(res.getId())) {
				player.getGameStats().addModifiers(res.getStatsEffectId(), res.getStats(), false);
			}
		}
		refreshLearnSkill(player, player);
	}
	
	@JsonIgnore
	public void refreshLearnSkill(Creature target, Player useSkiller) {
		for (CollectGeneralSkillCombiResource res : CollectManager.getInstance().collectGeneralSkillCombiResources.getAll()) {
			if (res.getGeneralResourceIds() != null) {
				boolean activeAll = true;
				for (String id : res.getGeneralResourceIds()) {
					if (!activeCollectGeneralIds.contains(id)) {
						activeAll = false;
						break;
					}
				}
				if (activeAll && !target.getEffectController().containsSkill(res.getSkillId()[useSkiller.getRole() - 1])) {
					Skill skill = SkillEngine.getInstance().getSkill(target, res.getSkillId()[useSkiller.getRole() - 1], 
							target.getObjectId(), 0, 0, target, null);
					skill.noEffectorUseSkill();
				}
			}
		}
	}
	
	public Map<String, Map<String, Integer>> getCollectedFamedGeneral() {
		return collectedFamedGeneral;
	}

	public void setCollectedFamedGeneral(Map<String, Map<String, Integer>> collectedFamedGeneral) {
		this.collectedFamedGeneral = collectedFamedGeneral;
	}
	
	@JsonIgnore
	public boolean hasCollect(String ownerKey, String id) {
		Map<String, Integer> collect = collectedFamedGeneral.get(ownerKey);
		if (collect == null) {
			return false;
		}
		Integer count = collect.get(id);
		if (count == null) {
			return false;
		}
		return count >= ItemManager.getInstance().getCreateSoulResource(id).getMaxCollectCount();
	}
	
	@JsonIgnore
	public int getFamedGeneralCollectNum(String ownerKey) {
		Map<String, Integer> collect = collectedFamedGeneral.get(ownerKey);
		int size = 0;
		if (collect == null) {
			return 0;
		}
		for (Integer count : collect.values()) {
			size += count;
		}
		return size;
	}
	
	@JsonIgnore
	public boolean collect(Player player, String ownerKey, String id) {
		if (hasCollect(ownerKey, id)) {
			return false;
		}
		if (!collectedFamedGeneral.containsKey(ownerKey)) {
			collectedFamedGeneral.put(ownerKey, new HashMap<String, Integer>());
		}
		Map<String, Integer> map = collectedFamedGeneral.get(ownerKey);
		Integer count = map.get(id);
		map.put(id, count == null ? 1 : count + 1);
		return true;
	}
	
	public HashSet<String> getActiveCollectGeneralIds() {
		return activeCollectGeneralIds;
	}

	public void setActiveCollectGeneralIds(HashSet<String> activeCollectGeneralIds) {
		this.activeCollectGeneralIds = activeCollectGeneralIds;
	}

}
