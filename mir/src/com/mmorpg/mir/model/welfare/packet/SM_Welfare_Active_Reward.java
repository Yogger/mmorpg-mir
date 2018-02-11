package com.mmorpg.mir.model.welfare.packet;

public class SM_Welfare_Active_Reward {

	private int activeValue;

	public static SM_Welfare_Active_Reward valueOf(int activeValue) {
		SM_Welfare_Active_Reward sm = new SM_Welfare_Active_Reward();
		sm.setActiveValue(activeValue);
		return sm;
	}

	public int getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(int activeValue) {
		this.activeValue = activeValue;
	}

}
