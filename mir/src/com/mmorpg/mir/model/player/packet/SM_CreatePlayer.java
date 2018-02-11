package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.player.entity.PlayerVO;

public class SM_CreatePlayer {
	private int code = 0;
	private PlayerVO playerVO;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public PlayerVO getPlayerVO() {
		return playerVO;
	}

	public void setPlayerVO(PlayerVO playerVO) {
		this.playerVO = playerVO;
	}
}
