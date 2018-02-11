package com.mmorpg.mir.model.gameobjects.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_PlayerGameStats {
	private Map<String, Long> stats;

	public static SM_PlayerGameStats valueOf(Map<StatEnum, Stat> currentStats) {
		SM_PlayerGameStats smp = new SM_PlayerGameStats();
		smp.setStats(new HashMap<String, Long>());
		for (Entry<StatEnum, Stat> entry : currentStats.entrySet()) {
			smp.getStats().put(entry.getKey().getName(), entry.getValue().getValue());
		}
		return smp;
	}

	public Map<String, Long> getStats() {
		return stats;
	}

	public void setStats(Map<String, Long> stats) {
		this.stats = stats;
	}

}
