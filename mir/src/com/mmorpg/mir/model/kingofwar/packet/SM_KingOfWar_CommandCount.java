package com.mmorpg.mir.model.kingofwar.packet;

import java.util.Map;

public class SM_KingOfWar_CommandCount {

	private Map<Integer, Integer> counts;

	public static SM_KingOfWar_CommandCount valueOf(Map<Integer, Integer> playerCounts) {
		SM_KingOfWar_CommandCount sm = new SM_KingOfWar_CommandCount();
		sm.counts = playerCounts;
		return sm;
	}

	public Map<Integer, Integer> getCounts() {
		return counts;
	}

	public void setCounts(Map<Integer, Integer> counts) {
		this.counts = counts;
	}

}
