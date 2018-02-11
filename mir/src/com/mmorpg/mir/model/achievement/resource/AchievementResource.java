package com.mmorpg.mir.model.achievement.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;

public class AchievementResource {
	@Id
	private int id;
	/** 获得成就条件 */
	private String conditionId;

	@Transient
	private CoreConditions conditions;

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			conditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionId);
		}
		return conditions;
	}

	@JsonIgnore
	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
