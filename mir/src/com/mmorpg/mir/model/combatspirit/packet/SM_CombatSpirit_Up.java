package com.mmorpg.mir.model.combatspirit.packet;

public class SM_CombatSpirit_Up {

	private int combatType;

	public int getCombatType() {
    	return combatType;
    }

	public void setCombatType(int combatType) {
    	this.combatType = combatType;
    }
	
	public static SM_CombatSpirit_Up valueOf(int t) {
		SM_CombatSpirit_Up sm = new SM_CombatSpirit_Up();
		sm.combatType = t;
		return sm;
	}
	
}
