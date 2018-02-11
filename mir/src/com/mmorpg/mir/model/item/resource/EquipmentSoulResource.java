package com.mmorpg.mir.model.item.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentSoulResource {
	@Id
	private String id;
	
	private String nextId;
	/** 激活条件 */
	private String[] conditionIds;
	/** 激活属性 */
	private Stat[] stats;

	private int statType;

	private int soulStatLevel;
	
	private double soulGoldFactor;
	
	private double soulTypefactor;

	private EquipmentType equipmentType;
	@Transient
	private CoreConditions conditions;

	public int getSoulStatLevel() {
		return soulStatLevel;
	}

	public void setSoulStatLevel(int soulStatLevel) {
		this.soulStatLevel = soulStatLevel;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (conditionIds == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
			}
		}
		return conditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public int getStatType() {
		return statType;
	}

	public void setStatType(int statType) {
		this.statType = statType;
	}

	public double getSoulGoldFactor() {
		return soulGoldFactor;
	}

	public void setSoulGoldFactor(double soulGoldFactor) {
		this.soulGoldFactor = soulGoldFactor;
	}

	public double getSoulTypefactor() {
		return soulTypefactor;
	}

	public void setSoulTypefactor(double soulTypefactor) {
		this.soulTypefactor = soulTypefactor;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

}
