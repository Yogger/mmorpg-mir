package com.mmorpg.mir.model.copy.resource;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CopyIndividualBossResource {

	@Id
	private String id;

	private String[] conditionIds;

	private String rewardId;

	@Transient
	private CoreConditions conditions;

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

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (ArrayUtils.isEmpty(conditionIds)) {
				conditions = new CoreConditions();
			} else {
				setConditions(CoreConditionManager.getInstance().getCoreConditions(1, conditionIds));
			}
		}
		return conditions;
	}

	@JsonIgnore
	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}
	
}
