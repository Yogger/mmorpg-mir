package com.mmorpg.mir.model.investigate.packet;

import java.util.Map;

public class SM_Query_Investigate_Status {

	private Map<Integer, Integer> map;

	public static SM_Query_Investigate_Status valueOf(Map<Integer, Integer> m) {
		SM_Query_Investigate_Status sm = new SM_Query_Investigate_Status();
		sm.map = m;
		return sm;
	}

	public Map<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}

}
