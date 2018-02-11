package com.mmorpg.mir.model.contact.packet;

import java.util.HashSet;


public class CM_ADD_FRIEND{

	private HashSet<Long> friendId;

	public HashSet<Long> getFriendId() {
		return friendId;
	}

	public void setFriendId(HashSet<Long> friendId) {
		this.friendId = friendId;
	}
	
}
