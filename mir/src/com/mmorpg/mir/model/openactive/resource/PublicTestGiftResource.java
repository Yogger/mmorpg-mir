package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class PublicTestGiftResource {
	@Id
	private String id;
	/** 购买条件 */
	private String[] conditonIds;
	/** 消耗Id */
	private String[] actionIds;
	/** 奖励Id */
	private String[] rewardIds;
	/** 组Id */
	private Integer groupId;
	/** 上一级别Id */
	private String lowLevelId;
	/** 下一级别Id */
	private String highLevelId;
	/** 活动版本 */
	private int version;

	@Transient
	private CoreActions coreActions;

	@Transient
	private CoreConditions coreConditions;

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

	public String[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(String[] actionIds) {
		this.actionIds = actionIds;
	}

	public String[] getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(String[] rewardIds) {
		this.rewardIds = rewardIds;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getLowLevelId() {
		return lowLevelId;
	}

	public void setLowLevelId(String lowLevelId) {
		this.lowLevelId = lowLevelId;
	}

	public String getHighLevelId() {
		return highLevelId;
	}

	public void setHighLevelId(String highLevelId) {
		this.highLevelId = highLevelId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@JsonIgnore
	public CoreActions getCoreActions() {
		if (null == coreActions) {
			coreActions = CoreActionManager.getInstance().getCoreActions(1, actionIds);
		}
		return coreActions;
	}

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (null == coreConditions) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditonIds);
		}
		return coreConditions;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
