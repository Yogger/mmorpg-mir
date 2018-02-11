package com.mmorpg.mir.model.capturetown.packet;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Buy_Motion {

	private int code;
	
	private int leftCount;

	public static SM_Buy_Motion valueOf(Player player) {
		SM_Buy_Motion sm = new SM_Buy_Motion();
		int left = TownConfig.getInstance().PLAYER_ENTER_DAILY_LIMIT.getValue() - 
				player.getPlayerCountryHistory().getCaptureTownInfo().getDailyCount();
		sm.leftCount = left; 
		return sm;
	}
	
	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
