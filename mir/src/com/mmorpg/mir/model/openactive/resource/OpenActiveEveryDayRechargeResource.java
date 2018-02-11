package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 每日充值
 * 
 * @author 37.com
 * 
 */
@Resource
public class OpenActiveEveryDayRechargeResource {
	@Id
	private String id;

	/** 奖励id */
	private String rewardChooserGroupId;

	/** 领取条件 */
	private String[] drawConditions;

	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, drawConditions);
		}
		return coreConditions;
	}

	@JsonIgnore
	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRewardChooserGroupId() {
		return rewardChooserGroupId;
	}

	public void setRewardChooserGroupId(String rewardChooserGroupId) {
		this.rewardChooserGroupId = rewardChooserGroupId;
	}

	public String[] getDrawConditions() {
		return drawConditions;
	}

	public void setDrawConditions(String[] drawConditions) {
		this.drawConditions = drawConditions;
	}

}
