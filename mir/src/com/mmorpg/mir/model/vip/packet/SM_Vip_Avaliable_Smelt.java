package com.mmorpg.mir.model.vip.packet;

public class SM_Vip_Avaliable_Smelt {

	private int leftDoubleNums;

	public int getLeftDoubleNums() {
    	return leftDoubleNums;
    }

	public void setLeftDoubleNums(int leftDoubleNums) {
    	this.leftDoubleNums = leftDoubleNums;
    }

	public static SM_Vip_Avaliable_Smelt valueOf(int c) {
		SM_Vip_Avaliable_Smelt sm = new SM_Vip_Avaliable_Smelt();
		sm.leftDoubleNums = c;
		return sm;
	}
}
