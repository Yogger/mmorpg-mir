package com.mmorpg.mir.model.express.packet;

import java.util.Map;

import com.mmorpg.mir.model.express.model.LorryNavigatorVO;

public class SM_Get_Current_Express {

	private Map<Integer, Map<Long, LorryNavigatorVO>> map;

	public static SM_Get_Current_Express valueOf(Map<Integer, Map<Long, LorryNavigatorVO>> m) {
		SM_Get_Current_Express sm = new SM_Get_Current_Express();
		sm.map = m;
		return sm;
	}

	public Map<Integer, Map<Long, LorryNavigatorVO>> getMap() {
    	return map;
    }

	public void setMap(Map<Integer, Map<Long, LorryNavigatorVO>> map) {
    	this.map = map;
    }
	
}
