package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.operator.model.ForbidChatList;

public class SGM_GetForbidChatList {
	private ForbidChatList forbidChatList;

	public ForbidChatList getForbidChatList() {
		return forbidChatList;
	}

	public void setForbidChatList(ForbidChatList forbidChatList) {
		this.forbidChatList = forbidChatList;
	}
}
