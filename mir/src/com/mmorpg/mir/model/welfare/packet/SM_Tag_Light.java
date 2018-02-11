package com.mmorpg.mir.model.welfare.packet;

import java.util.Map;

public class SM_Tag_Light {
	private Map<Integer, Boolean> status;

	public Map<Integer, Boolean> getStatus() {
    	return status;
    }

	public void setStatus(Map<Integer, Boolean> status) {
    	this.status = status;
    }
	
	public static SM_Tag_Light valueOf(Map<Integer, Boolean> m) {
		SM_Tag_Light sm = new SM_Tag_Light();
		sm.status = m;
		return sm;
	}
}
