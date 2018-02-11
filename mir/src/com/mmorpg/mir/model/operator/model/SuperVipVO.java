package com.mmorpg.mir.model.operator.model;

public class SuperVipVO {
	private String name;
	private String contact;
	private boolean superVip;
	private String picturePath;
	private long totalCharge;
	private long firstChargeTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public long getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(long totalCharge) {
		this.totalCharge = totalCharge;
	}

	public long getFirstChargeTime() {
		return firstChargeTime;
	}

	public void setFirstChargeTime(long firstChargeTime) {
		this.firstChargeTime = firstChargeTime;
	}

	public boolean isSuperVip() {
		return superVip;
	}

	public void setSuperVip(boolean superVip) {
		this.superVip = superVip;
	}

}
