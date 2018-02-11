package com.mmorpg.mir.model.country.packet;

public class SM_Country_QuestOpen {
	private byte openType;
	private long endTime;

	public static SM_Country_QuestOpen valueOf(byte openType, long end) {
		SM_Country_QuestOpen sm = new SM_Country_QuestOpen();
		sm.endTime = end;
		sm.openType = openType;
		return sm;
	}

	public byte getOpenType() {
		return openType;
	}

	public void setOpenType(byte openType) {
		this.openType = openType;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
