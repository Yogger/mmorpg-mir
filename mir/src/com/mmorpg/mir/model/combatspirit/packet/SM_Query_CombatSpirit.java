package com.mmorpg.mir.model.combatspirit.packet;

import java.util.Map;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage;

public class SM_Query_CombatSpirit {
	private Map<Integer, CombatSpirit> combatSpirits;
	private int killerHistory;

	public static SM_Query_CombatSpirit valueOf(CombatSpiritStorage storage) { 
		SM_Query_CombatSpirit sm = new SM_Query_CombatSpirit();
		sm.combatSpirits = storage.getCombatSpiritCollection();
		sm.killerHistory = storage.getKilledHistory();
		return sm;
	}
	
	public Map<Integer, CombatSpirit> getCombatSpirits() {
    	return combatSpirits;
    }

	public void setCombatSpirits(Map<Integer, CombatSpirit> combatSpirits) {
    	this.combatSpirits = combatSpirits;
    }

	public int getKillerHistory() {
		return killerHistory;
	}

	public void setKillerHistory(int killerHistory) {
		this.killerHistory = killerHistory;
	}
	
}
