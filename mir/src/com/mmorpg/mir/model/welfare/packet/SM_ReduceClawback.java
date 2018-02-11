package com.mmorpg.mir.model.welfare.packet;

public class SM_ReduceClawback {

	private int eventId;
	private int currentCount;
	
	public static SM_ReduceClawback valueOf(int eId, int cCount) {
		SM_ReduceClawback sm = new SM_ReduceClawback();
		sm.eventId = eId;
		sm.currentCount = cCount;
		return sm;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
}
