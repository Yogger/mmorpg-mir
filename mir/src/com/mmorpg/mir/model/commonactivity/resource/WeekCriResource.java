package com.mmorpg.mir.model.commonactivity.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class WeekCriResource {

	@Id
	private Integer id;

	private String rewardId;

	private String actionId;

	private String i18name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getI18name() {
		return i18name;
	}

	public void setI18name(String i18name) {
		this.i18name = i18name;
	}

}
