package com.mmorpg.mir.model.operator.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class QiHu360PrivilegeResource {
	@Id
	private String id;

	/** 领取奖励的条件 */
	private String[] rewardConditions;
	/** 领取奖励的Id */
	private String[] rewardIds;

	/** 0-领取一次,1-每天 */
	private int everyday;

	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public boolean isEveryDay() {
		return 1 == everyday;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getRewardConditions() {
		return rewardConditions;
	}

	public void setRewardConditions(String[] rewardConditions) {
		this.rewardConditions = rewardConditions;
	}

	public String[] getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(String[] rewardIds) {
		this.rewardIds = rewardIds;
	}

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (null == coreConditions) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, rewardConditions);
		}
		return coreConditions;
	}

	public int getEveryday() {
		return everyday;
	}

	public void setEveryday(int everyday) {
		this.everyday = everyday;
	}
}
