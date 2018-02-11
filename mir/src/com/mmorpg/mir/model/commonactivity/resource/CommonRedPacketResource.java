package com.mmorpg.mir.model.commonactivity.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonRedPacketResource {

	public static final String ACTIVITY_NAME = "ACTIVITY_NAME";

	@Id
	private String id;

	@Index(name = ACTIVITY_NAME)
	private String activityName;

	private String[] rewardConds;

	private String chooserGroupId;

	@JsonIgnore
	public CoreConditions getRewardConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.rewardConds);
	}

	public String getId() {
		return id;
	}

	public String getActivityName() {
		return activityName;
	}

	public String[] getRewardConds() {
		return rewardConds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setRewardConds(String[] rewardConds) {
		this.rewardConds = rewardConds;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

}
