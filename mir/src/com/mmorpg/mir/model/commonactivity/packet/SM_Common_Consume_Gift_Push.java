package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Consume_Gift_Push {
	private String activeName;
	
	private int consumeCount;

	public static SM_Common_Consume_Gift_Push valueOf(String activeName, int consumeCount){
		SM_Common_Consume_Gift_Push sm = new SM_Common_Consume_Gift_Push();
		sm.activeName = activeName;
		sm.consumeCount = consumeCount;
		return sm;
	}
	
	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public int getConsumeCount() {
		return consumeCount;
	}

	public void setConsumeCount(int consumeCount) {
		this.consumeCount = consumeCount;
	}
}
