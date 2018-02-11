package com.mmorpg.mir.model.warship.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class WarshipResource {

	public static final int REWARD_WHITE = 0;
	public static final int REWARD_GREEN = 1;
	public static final int REWARD_PURPLE = 2;
	public static final int REWARD_ORANGE = 3;
	
	@Id
	private int id;
	
	private int refreshSuccRate;

	private String[] warshipRefreshActionIds;
	
	private String[] warshipGoldRefreshActionIds;
	
	private String[] warshipConditionIds;

	private String[] warshipActionIds;

	private String rewardChooserGroup;
	
	@Transient
	private CoreActions coreActions;
	
	@Transient
	private CoreConditions coreConditions;
	
	@Transient
	private CoreActions refreshingActions;
	
	@Transient
	private CoreActions refreshingGoldActions;
	
	@JsonIgnore
	public CoreActions getWarshipActions() {
		if (coreActions == null) {
			coreActions = CoreActionManager.getInstance().getCoreActions(1, warshipActionIds);
		}
		return coreActions;
	}
	
	@JsonIgnore
	public CoreActions getRefreshingActions() {
		if (refreshingActions == null) {
			refreshingActions = CoreActionManager.getInstance().getCoreActions(1, warshipRefreshActionIds);
		}
		return refreshingActions;
	}
	
	@JsonIgnore
	public CoreActions getRefreshingGoldActions() {
		if (refreshingGoldActions == null) {
			refreshingGoldActions = CoreActionManager.getInstance().getCoreActions(1, warshipGoldRefreshActionIds);
		}
		return refreshingGoldActions;
	}
	
	@JsonIgnore
	public CoreConditions getWarshipCoreConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, warshipConditionIds);
		}
		return coreConditions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getWarshipConditionIds() {
		return warshipConditionIds;
	}

	public void setWarshipConditionIds(String[] warshipConditionIds) {
		this.warshipConditionIds = warshipConditionIds;
	}

	public String[] getWarshipActionIds() {
		return warshipActionIds;
	}

	public void setWarshipActionIds(String[] warshipActionIds) {
		this.warshipActionIds = warshipActionIds;
	}

	public String getRewardChooserGroup() {
		return rewardChooserGroup;
	}

	public void setRewardChooserGroup(String rewardChooserGroup) {
		this.rewardChooserGroup = rewardChooserGroup;
	}

	public int getRefreshSuccRate() {
		return refreshSuccRate;
	}

	public void setRefreshSuccRate(int refreshSuccRate) {
		this.refreshSuccRate = refreshSuccRate;
	}

	public String[] getWarshipRefreshActionIds() {
		return warshipRefreshActionIds;
	}

	public void setWarshipRefreshActionIds(String[] warshipRefreshActionIds) {
		this.warshipRefreshActionIds = warshipRefreshActionIds;
	}

	public String[] getWarshipGoldRefreshActionIds() {
		return warshipGoldRefreshActionIds;
	}

	public void setWarshipGoldRefreshActionIds(String[] warshipGoldRefreshActionIds) {
		this.warshipGoldRefreshActionIds = warshipGoldRefreshActionIds;
	}

}
