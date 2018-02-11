package com.mmorpg.mir.model.welfare.packet;


public class SM_GiftCollect_Finish {
	private String id;
	private long leftAmount;
	private long totalAmount;

	public static SM_GiftCollect_Finish valueOf(String id, long leftAmount, long totalAmount) {
		SM_GiftCollect_Finish result = new SM_GiftCollect_Finish();
		result.id = id;
		result.leftAmount = leftAmount;
		result.totalAmount = totalAmount;
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
