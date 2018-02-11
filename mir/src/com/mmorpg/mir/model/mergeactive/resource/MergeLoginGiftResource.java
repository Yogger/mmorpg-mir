package com.mmorpg.mir.model.mergeactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MergeLoginGiftResource {
	@Id
	private String id;
	/** 领取条件 */
	private String[] conditonIds;
	/** 奖励Id */
	private String rewardIds;
	/** 和服的天数 */
	private int mergeDate;

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

	public String getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(String rewardIds) {
		this.rewardIds = rewardIds;
	}

	public int getMergeDate() {
		return mergeDate;
	}

	public void setMergeDate(int mergeDate) {
		this.mergeDate = mergeDate;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}
}
