package com.mmorpg.mir.model.group.packet;

public class SM_REQUEST_GROUP_INVITE {
	private String name;

	public static SM_REQUEST_GROUP_INVITE valueOf(String name) {
		SM_REQUEST_GROUP_INVITE sm = new SM_REQUEST_GROUP_INVITE();
		sm.name = name;
		return sm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
