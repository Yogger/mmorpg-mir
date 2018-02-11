package com.mmorpg.mir.model.temple.packet;

import java.util.Map;

public class SM_Query_Temple_Status {

	private Map<Integer, Integer> map;

	public static SM_Query_Temple_Status valueOf(Map<Integer, Integer> m) {
		SM_Query_Temple_Status sm = new SM_Query_Temple_Status();
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
