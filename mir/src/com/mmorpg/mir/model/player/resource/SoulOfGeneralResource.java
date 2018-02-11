package com.mmorpg.mir.model.player.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SoulOfGeneralResource {
	
	public static final StatEffectId GENERAL_SPIRIT = StatEffectId.valueOf("GENERAL_SPIRIT", StatEffectType.GENERAL_SPIRIT);

	@Id
	private int id;
	/** 条件ID **/
	private String[] conditionIds;
	/** 获得的属性 **/
	private Stat[] archerStats;
	private Stat[] strategistStats;
	private Stat[] warriorStats;

	@JsonIgnore
	public Stat[] getRoleStat(int role) {
		switch (role) {
		case 1:
			return warriorStats;
		case 2:
			return archerStats;
		case 3:
			return strategistStats;
		default:
			break;
		}
		return null;
	}
	
	@Transient
	private CoreConditions conditions;
	
	@JsonIgnore
	public CoreConditions getLightCondition() {
		if (conditions == null) {
			if (conditionIds == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
			}
		}
		return conditions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public Stat[] getArcherStats() {
		return archerStats;
	}

	public void setArcherStats(Stat[] archerStats) {
		this.archerStats = archerStats;
	}

	public Stat[] getStrategistStats() {
		return strategistStats;
	}

	public void setStrategistStats(Stat[] strategistStats) {
		this.strategistStats = strategistStats;
	}

	public Stat[] getWarriorStats() {
		return warriorStats;
	}

	public void setWarriorStats(Stat[] warriorStats) {
		this.warriorStats = warriorStats;
	}

}
