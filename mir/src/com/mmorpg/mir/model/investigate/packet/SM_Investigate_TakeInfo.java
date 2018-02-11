package com.mmorpg.mir.model.investigate.packet;

public class SM_Investigate_TakeInfo {
	private String id;
	private long lastChangeTime;
	private boolean useGold;

	public static SM_Investigate_TakeInfo valueOf(String id, long lastChangeTime, boolean useGold) {
		SM_Investigate_TakeInfo sm = new SM_Investigate_TakeInfo();
		sm.id = id;
		sm.lastChangeTime = lastChangeTime;
		sm.useGold = useGold;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public boolean isUseGold() {
    	return useGold;
    }

	public void setUseGold(boolean useGold) {
    	this.useGold = useGold;
    }

}
