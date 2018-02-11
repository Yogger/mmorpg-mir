package com.mmorpg.mir.model.gameobjects.stats;

import java.util.Map;

import org.h2.util.New;

public class PlayerGameStatsVO {
	private Map<String, Long> stats = New.hashMap();

	public Map<String, Long> getStats() {
		return stats;
	}

	public void setStats(Map<String, Long> stats) {
		this.stats = stats;
	}

}
