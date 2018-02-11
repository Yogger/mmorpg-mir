package com.mmorpg.mir.model.lifegrid.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class LifeGridConvertResource {

	@Id
	private String id;

	private String rewardId;

	private String[] conditions;

	private int actPoint;

	@Transient
	private transient CoreConditions conds;

	@JsonIgnore
	public CoreConditions getConds() {
		if (this.conds == null) {
			this.conds = CoreConditionManager.getInstance().getCoreConditions(1, this.conditions);
		}
		return this.conds;
	}

	public String getId() {
		return id;
	}

	public String getRewardId() {
		return rewardId;
	}

	public String[] getConditions() {
		return conditions;
	}

	public int getActPoint() {
		return actPoint;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	public void setActPoint(int actPoint) {
		this.actPoint = actPoint;
	}

	@JsonIgnore
	public void setConds(CoreConditions conds) {
		this.conds = conds;
	}

}
