package com.mmorpg.mir.model.commonactivity.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonFireCelebrateResource {

	@Id
	private String id;

	private String[] attendConds;

	private String[] actions;

	private String rewardIds;

	@JsonIgnore
	public CoreConditions getAttendCondition() {
		return CoreConditionManager.getInstance().getCoreConditions(1, attendConds);
	}

	@JsonIgnore
	public CoreActions getActions(int count) {
		return CoreActionManager.getInstance().getCoreActions(count, actions);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getAttendConds() {
		return attendConds;
	}

	public void setAttendConds(String[] attendConds) {
		this.attendConds = attendConds;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(String rewardIds) {
		this.rewardIds = rewardIds;
	}


}
