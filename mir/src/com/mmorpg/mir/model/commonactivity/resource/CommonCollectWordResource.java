package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonCollectWordResource {
	public static final String ACTIVE_NAME = "ACTIVE_NAME";

	@Id
	private String id;
	@Index(name = ACTIVE_NAME)
	private String activeName;
	/** 鉴宝条件 */
	private String[] conditonIds;
	/** 消耗Id */
	private String[] actionIds;
	/**　奖励Id*/
	private String rewardId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActiveName() {
		return activeName;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public String[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(String[] actionIds) {
		this.actionIds = actionIds;
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

	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public CoreConditions getCoreConditions(int size) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(size, conditonIds);
		return coreConditions;
	}

	@Transient
	private CoreActions coreActions;

	@JsonIgnore
	public CoreActions getCoreActions(int size) {
			coreActions = CoreActionManager.getInstance().getCoreActions(size, actionIds);
		return coreActions;
	}
	
}
