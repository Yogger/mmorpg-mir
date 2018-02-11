package com.mmorpg.mir.model.gascopy.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_GasValue_Change {

	private int gasValue;
	
	public static SM_GasValue_Change valueOf(Player player) {
		SM_GasValue_Change sm = new SM_GasValue_Change();
		sm.gasValue = player.getGasCopy().getGasValue();
		return sm;
	}
	
	public int getGasValue() {
		return gasValue;
	}

	public void setGasValue(int gasValue) {
		this.gasValue = gasValue;
	}
	
}
