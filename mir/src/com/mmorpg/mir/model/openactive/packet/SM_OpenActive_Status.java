package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

public class SM_OpenActive_Status {

	private Map<Integer, Boolean> status;
	
	public static SM_OpenActive_Status valueOf(Map<Integer, Boolean> ret) {
		SM_OpenActive_Status sm = new SM_OpenActive_Status();
		sm.status = ret;
		return sm;
	}

	public Map<Integer, Boolean> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, Boolean> status) {
		this.status = status;
	}

}
