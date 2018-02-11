package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.admin.bean.PlayerInfoBean;

public class SGM_GetPlayerInfo {
	private PlayerInfoBean playerInfoBean;

	public PlayerInfoBean getPlayerInfoBean() {
		return playerInfoBean;
	}

	public void setPlayerInfoBean(PlayerInfoBean playerInfoBean) {
		this.playerInfoBean = playerInfoBean;
	}

}
