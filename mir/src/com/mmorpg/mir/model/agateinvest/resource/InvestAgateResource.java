package com.mmorpg.mir.model.agateinvest.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.agateinvest.model.InvestAgateType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class InvestAgateResource {

	@Id
	private String id;

	/** 投资类型 */
	private InvestAgateType investType;

	/** 对应奖励id */
	private String rewardId;

	/** 领取条件 */
	private String[] drawConditionIds;

	@Transient
	private CoreConditions conditions;

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			conditions = CoreConditionManager.getInstance().getCoreConditions(1, drawConditionIds);
		}
		return conditions;
	}

	@JsonIgnore
	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InvestAgateType getInvestType() {
		return investType;
	}

	public void setInvestType(InvestAgateType investType) {
		this.investType = investType;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public String[] getDrawConditionIds() {
		return drawConditionIds;
	}

	public void setDrawConditionIds(String[] drawConditionIds) {
		this.drawConditionIds = drawConditionIds;
	}
	
}
