package com.mmorpg.mir.model.contact.packet;

import com.mmorpg.mir.model.contact.model.SocialNetData;

public class SM_Player_Social_Data {

	private int code;
	private SocialNetData friend;
	
	public static SM_Player_Social_Data valueOf(int code, SocialNetData data) {
		SM_Player_Social_Data sm = new SM_Player_Social_Data();
		sm.code = code;
		sm.friend = data;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public SocialNetData getFriend() {
		return friend;
	}

	public void setFriend(SocialNetData friend) {
		this.friend = friend;
	}
	
}
