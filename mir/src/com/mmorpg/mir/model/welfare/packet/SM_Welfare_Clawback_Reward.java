package com.mmorpg.mir.model.welfare.packet;

public class SM_Welfare_Clawback_Reward {

	private int eventId;// 追回成功的事件Id如果失败不会收到这条消息

	public static SM_Welfare_Clawback_Reward valueOf(int eventId){
		SM_Welfare_Clawback_Reward sm = new SM_Welfare_Clawback_Reward();
		sm.setEventId(eventId);
		return sm;
	}
	
	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

}
