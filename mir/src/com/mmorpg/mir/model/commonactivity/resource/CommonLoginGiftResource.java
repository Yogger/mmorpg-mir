package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonLoginGiftResource {
	private static final String INDEX_ACTIVITYNAME = "INDEX_ACTIVITYNAME";

	@Id
	private String id;
	@Index(name = INDEX_ACTIVITYNAME)
	private String activeName;
	/** 领取条件 */
	private String[] conditonIds;
	/** 奖励Id */
	private String rewardId;

	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (null == coreConditions) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditonIds);
		}
		return coreConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}
	
}
