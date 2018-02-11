package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

public class SM_Query_Military_Activity {
	private Map<String, Integer> serverAvailiable;
	private int rank;

	public static SM_Query_Military_Activity valueOf(Map<String, Integer> map, int rank) {
		SM_Query_Military_Activity sm = new SM_Query_Military_Activity();
		sm.serverAvailiable = map;
		sm.rank = rank;
		return sm;
	}
	
	public Map<String, Integer> getServerAvailiable() {
		return serverAvailiable;
	}

	public void setServerAvailiable(Map<String, Integer> serverAvailiable) {
		this.serverAvailiable = serverAvailiable;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
