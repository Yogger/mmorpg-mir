package com.mmorpg.mir.model.welfare.packet;

public class SM_Recieve_Gift {

	private int selfAmount;
	
	public static SM_Recieve_Gift valueOf(int self) {
		SM_Recieve_Gift sm = new SM_Recieve_Gift();
		sm.selfAmount = self;
		return sm;
	}

	public int getSelfAmount() {
		return selfAmount;
	}

	public void setSelfAmount(int selfAmount) {
		this.selfAmount = selfAmount;
	}

}
