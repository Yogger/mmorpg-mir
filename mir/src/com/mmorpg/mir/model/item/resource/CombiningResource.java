package com.mmorpg.mir.model.item.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CombiningResource {

	@Id
	private String id;
	private String[] conditionId;
	private String[] actionId;
	private String[] rewardId;
	
	private CoreActionResource[] assistantAct;
	private CoreActionResource[] assistantGoldAct;
	private int addRate;
	
	private int successRate;

	@Transient
	private CoreConditions coreConditions;

	@Transient
	private CoreActions coreActions;

	@JsonIgnore
	public CoreConditions getCondition() {
		if (coreConditions == null) {
			if (conditionId == null) {
				coreConditions = new CoreConditions();
			} else {
				coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionId);
			}
		}
		return coreConditions;
	}

	@JsonIgnore
	public CoreActions getActions(int count) {
		if (coreActions == null) {
			if (actionId == null) {
				coreActions = new CoreActions();
			} else {
				coreActions = CoreActionManager.getInstance().getCoreActions(1, actionId);
			}
		}
		if (count == 1) {
			return coreActions;
		}
		return CoreActionManager.getInstance().getCoreActions(count, actionId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditionId() {
		return conditionId;
	}

	public void setConditionId(String[] conditionId) {
		this.conditionId = conditionId;
	}

	public String[] getActionId() {
		return actionId;
	}

	public void setActionId(String[] actionId) {
		this.actionId = actionId;
	}

	public int getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}


	public int getAddRate() {
		return addRate;
	}

	public void setAddRate(int addRate) {
		this.addRate = addRate;
	}

	public String[] getRewardId() {
    	return rewardId;
    }

	public void setRewardId(String[] rewardId) {
    	this.rewardId = rewardId;
    }

	public CoreActionResource[] getAssistantAct() {
    	return assistantAct;
    }

	public void setAssistantAct(CoreActionResource[] assistantAct) {
    	this.assistantAct = assistantAct;
    }

	public CoreActionResource[] getAssistantGoldAct() {
    	return assistantGoldAct;
    }

	public void setAssistantGoldAct(CoreActionResource[] assistantGoldAct) {
    	this.assistantGoldAct = assistantGoldAct;
    }

}
