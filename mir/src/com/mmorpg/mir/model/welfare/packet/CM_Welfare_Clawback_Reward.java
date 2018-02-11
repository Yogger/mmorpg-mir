package com.mmorpg.mir.model.welfare.packet;

public class CM_Welfare_Clawback_Reward {

	private int eventId; // 事件Id
	private int currecyType;// 1使用铜币追回 2.使用元宝追回

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getCurrecyType() {
		return currecyType;
	}

	public void setCurrecyType(int currecyType) {
		this.currecyType = currecyType;
	}
}
