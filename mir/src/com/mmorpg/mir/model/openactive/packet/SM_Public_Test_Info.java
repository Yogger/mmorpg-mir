package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Public_Test_Info {
	private Map<Integer, String> showInfo;

	public static SM_Public_Test_Info valueOf(Player player) {
		SM_Public_Test_Info result = new SM_Public_Test_Info();
		result.setShowInfo(player.getOpenActive().getGiftActive().getFrontShowGift(player));
		return result;
	}

	public Map<Integer, String> getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(Map<Integer, String> showInfo) {
		this.showInfo = showInfo;
	}
}
