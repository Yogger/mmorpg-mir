package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

public class SM_Server_Limit_Reward {

	private Map<String, Integer> limitMap;

	public static SM_Server_Limit_Reward valueOf(Map<String, Integer> ret) {
		SM_Server_Limit_Reward sm = new SM_Server_Limit_Reward();
		sm.limitMap = ret;
		return sm;
	}
	
	public Map<String, Integer> getLimitMap() {
		return limitMap;
	}

	public void setLimitMap(Map<String, Integer> limitMap) {
		this.limitMap = limitMap;
	}

}

