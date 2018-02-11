package com.mmorpg.mir.admin.packet;

public class SGM_GetPlayerInfo_New {
	private String playerInfoJson;

	public static SGM_GetPlayerInfo_New valueOf(String playerInfoJson) {
		SGM_GetPlayerInfo_New result = new SGM_GetPlayerInfo_New();
		result.playerInfoJson = playerInfoJson;
		return result;
	}

	public String getPlayerInfoJson() {
		return playerInfoJson;
	}

	public void setPlayerInfoJson(String playerInfoJson) {
		this.playerInfoJson = playerInfoJson;
	}

}
