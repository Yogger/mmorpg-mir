package com.mmorpg.mir.model.welfare.packet;

public class SM_GiftCollect_Open {
	private long leftAmount;
	private long totalAmount;

	public static SM_GiftCollect_Open valueOf(long leftAmount, long totalAmount) {
		SM_GiftCollect_Open result = new SM_GiftCollect_Open();
		result.leftAmount = leftAmount;
		result.totalAmount = totalAmount;
		return result;
	}

	public long getLeftAmount() {
		return leftAmount;
	}

	public void setLeftAmount(long leftAmount) {
		this.leftAmount = leftAmount;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

}
