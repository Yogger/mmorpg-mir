package com.mmorpg.mir.model.world.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_PlayerSimple_All {

	private ArrayList<PlayerSimpleInfo> infos;

	public static SM_PlayerSimple_All valueOf(ArrayList<PlayerSimpleInfo> all) {
		SM_PlayerSimple_All sm = new SM_PlayerSimple_All();
		sm.infos = all;
		return sm;
	}

	public ArrayList<PlayerSimpleInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<PlayerSimpleInfo> infos) {
		this.infos = infos;
	}

}
