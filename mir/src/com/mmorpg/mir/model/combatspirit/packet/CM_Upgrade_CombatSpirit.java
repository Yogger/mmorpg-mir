package com.mmorpg.mir.model.combatspirit.packet;

public class CM_Upgrade_CombatSpirit {

	private int combatSpiritType;
	
	private boolean auto;

	public final int getCombatSpiritType() {
    	return combatSpiritType;
    }

	public final void setCombatSpiritType(int combatSpiritType) {
    	this.combatSpiritType = combatSpiritType;
    }

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

}
