package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

public class SM_Query_Level_Activity {
	private Map<String, Integer> serverAvailiable;
	private int maxLevel;
	private int rank;

	public static SM_Query_Level_Activity valueOf(Map<String, Integer> map, int maxLevel, int rank) {
		SM_Query_Level_Activity sm = new SM_Query_Level_Activity();
		sm.serverAvailiable = map;
		sm.maxLevel = maxLevel;
		sm.rank = rank;
		return sm;
	}
	
	public Map<String, Integer> getServerAvailiable() {
		return serverAvailiable;
	}

	public void setServerAvailiable(Map<String, Integer> serverAvailiable) {
		this.serverAvailiable = serverAvailiable;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
