package com.mmorpg.mir.model.combatspirit.packet;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;


public class SM_Upgrade_CombatSpirit {

	private CombatSpirit combatSpirit;

	public static Object valueOf(CombatSpirit combat) {
		SM_Upgrade_CombatSpirit sm = new SM_Upgrade_CombatSpirit();
		sm.combatSpirit = combat;
		return sm;
	}

	public CombatSpirit getCombatSpirit() {
		return combatSpirit;
	}

	public void setCombatSpirit(CombatSpirit combatSpirit) {
		this.combatSpirit = combatSpirit;
	}

}
