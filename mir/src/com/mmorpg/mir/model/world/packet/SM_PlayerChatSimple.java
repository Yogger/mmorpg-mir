package com.mmorpg.mir.model.world.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.player.model.PlayerChatSimple;

public class SM_PlayerChatSimple {

	private ArrayList<PlayerChatSimple> infos;

	public static SM_PlayerChatSimple valueOf(ArrayList<PlayerChatSimple> all) {
		SM_PlayerChatSimple sm = new SM_PlayerChatSimple();
		sm.infos = all;
		return sm;
	}

	public ArrayList<PlayerChatSimple> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<PlayerChatSimple> infos) {
		this.infos = infos;
	}

}
