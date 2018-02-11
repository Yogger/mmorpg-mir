package com.mmorpg.mir.model.capturetown.packet;

import com.mmorpg.mir.model.capturetown.model.PlayerCaptureTownInfo;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Reset_PlayerTownInfo {
	
	private PlayerCaptureTownInfo info;
	
	public static SM_Reset_PlayerTownInfo valueOf(Player player) {
		SM_Reset_PlayerTownInfo sm = new SM_Reset_PlayerTownInfo();
		sm.info = player.getPlayerCountryHistory().getCaptureTownInfo();
		return sm;
	}

	public PlayerCaptureTownInfo getInfo() {
		return info;
	}

	public void setInfo(PlayerCaptureTownInfo info) {
		this.info = info;
	}
	
}
