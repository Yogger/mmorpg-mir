package com.mmorpg.mir.model.openactive.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 集字活动
 * 
 * @author 37.com
 * 
 */
@Resource
public class OpenActiveCollectItemResource {

	@Id
	private String id;

	/** 领取条件 */
	private String[] conditionIds;

	private String[] actionIds;

	private String rewardId;

	@JsonIgnore
	public CoreConditions getCondtions(int size) {
		return CoreConditionManager.getInstance().getCoreConditions(size, conditionIds);
	}

	@JsonIgnore
	public CoreActions getActions(int size) {
		return CoreActionManager.getInstance().getCoreActions(size, actionIds);
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

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public String[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(String[] actionIds) {
		this.actionIds = actionIds;
	}

}
