package com.mmorpg.mir.model.player.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_PlayerSimple_Map {
	private ArrayList<PlayerSimpleInfo> infos = new ArrayList<PlayerSimpleInfo>();

	public ArrayList<PlayerSimpleInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<PlayerSimpleInfo> infos) {
		this.infos = infos;
	}

}
