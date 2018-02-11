package com.mmorpg.mir.model.group.packet;

public class SM_REJECT_GROUP_INVITE {
	private String name;
	private int code;
	
	public static SM_REJECT_GROUP_INVITE valueOf(String name, int c) {
		SM_REJECT_GROUP_INVITE sm = new SM_REJECT_GROUP_INVITE();
		sm.name = name;
		sm.code = c;
		return sm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	int getCode() {
    	return code;
    }

	void setCode(int code) {
    	this.code = code;
    }
	
}
