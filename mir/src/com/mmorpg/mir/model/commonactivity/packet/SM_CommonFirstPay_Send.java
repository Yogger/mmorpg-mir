package com.mmorpg.mir.model.commonactivity.packet;

public class SM_CommonFirstPay_Send {
	private String activeName;
	
	private long payCount;

	public static SM_CommonFirstPay_Send valueOf(String activeName, long payCount){
		SM_CommonFirstPay_Send sm = new SM_CommonFirstPay_Send();
		sm.activeName = activeName;
		sm.payCount = payCount;
		return sm;
	}
	
	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}
}
