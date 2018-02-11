package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SGM_GetSimplePlayerInfoByAccount {
	private PlayerSimpleInfo playerSimpleInfo;

	public PlayerSimpleInfo getPlayerSimpleInfo() {
		return playerSimpleInfo;
	}

	public void setPlayerSimpleInfo(PlayerSimpleInfo playerSimpleInfo) {
		this.playerSimpleInfo = playerSimpleInfo;
	}

}
