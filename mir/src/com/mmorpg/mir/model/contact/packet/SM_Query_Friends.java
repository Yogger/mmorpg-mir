package com.mmorpg.mir.model.contact.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_Query_Friends {

	private ArrayList<PlayerSimpleInfo> friends;
	
	public static SM_Query_Friends valueOf(ArrayList<PlayerSimpleInfo> infos) {
		SM_Query_Friends sm = new SM_Query_Friends();
		sm.friends = infos;
		return sm;
	}

	public ArrayList<PlayerSimpleInfo> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<PlayerSimpleInfo> friends) {
		this.friends = friends;
	}
	
}
