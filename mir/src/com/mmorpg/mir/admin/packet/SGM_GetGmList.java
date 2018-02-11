package com.mmorpg.mir.admin.packet;

import java.util.HashSet;
import java.util.Set;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SGM_GetGmList {
	private Set<PlayerSimpleInfo> gmList;

	public static SGM_GetGmList valueOf() {
		SGM_GetGmList sgm = new SGM_GetGmList();
		sgm.gmList = new HashSet<PlayerSimpleInfo>();
		return sgm;
	}

	public void add(Player player) {
		gmList.add(player.createSimple());
	}

	public Set<PlayerSimpleInfo> getGmList() {
		return gmList;
	}

	public void setGmList(Set<PlayerSimpleInfo> gmList) {
		this.gmList = gmList;
	}

}
