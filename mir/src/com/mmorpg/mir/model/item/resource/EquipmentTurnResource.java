package com.mmorpg.mir.model.item.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentTurnResource {
	@Id
	private String id;

	// 转生次数
	private int turn;

	private EquipmentType equipmentType;

	private int quality;

	private int level;

	private int role;

	private int specialType;

	private Stat[] stats;

	private String smeltRewardId;

	private CoreActionResource[] coreActionsResources;

	private int rate;

	private CoreActionResource[] addRateActions;

	private String i18name;

	@JsonIgnore
	public CoreActions getCoreActions() {
		return CoreActionManager.getInstance().getCoreActions(1, coreActionsResources);
	}

	@JsonIgnore
	public CoreActions getRateActions() {
		return CoreActionManager.getInstance().getCoreActions(1, addRateActions);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public CoreActionResource[] getCoreActionsResources() {
		return coreActionsResources;
	}

	public void setCoreActionsResources(CoreActionResource[] coreActionsResources) {
		this.coreActionsResources = coreActionsResources;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getI18name() {
		return i18name;
	}

	public void setI18name(String i18name) {
		this.i18name = i18name;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String getSmeltRewardId() {
		return smeltRewardId;
	}

	public void setSmeltRewardId(String smeltRewardId) {
		this.smeltRewardId = smeltRewardId;
	}

}
