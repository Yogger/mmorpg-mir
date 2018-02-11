package com.mmorpg.mir.model.capturetown.packet;

import java.util.Map;

public class SM_Refresh_Town {
	private Map<String, Integer> townInfos;
	
	public static SM_Refresh_Town valueOf(Map<String, Integer> map) {
		SM_Refresh_Town sm = new SM_Refresh_Town();
		sm.townInfos = map;
		return sm;
	}

	public Map<String, Integer> getTownInfos() {
		return townInfos;
	}

	public void setTownInfos(Map<String, Integer> townInfos) {
		this.townInfos = townInfos;
	}
	
}
