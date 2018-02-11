package com.mmorpg.mir.model.welfare.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class OnlineResource {

	@Id
	private int id;
	private int onlineTimeMinutes;
	private String groupId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOnlineTimeMinutes() {
		return onlineTimeMinutes;
	}

	public void setOnlineTimeMinutes(int onlineTimeMinutes) {
		this.onlineTimeMinutes = onlineTimeMinutes;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
