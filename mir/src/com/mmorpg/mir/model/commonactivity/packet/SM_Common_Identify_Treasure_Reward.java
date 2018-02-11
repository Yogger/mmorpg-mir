package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Identify_Treasure_Reward {
	private String activeName;

	private int currentLuckValue;
	
	private String itemId;
	
	private int errorCode;
	
	public static SM_Common_Identify_Treasure_Reward valueOf(String activeName){
		SM_Common_Identify_Treasure_Reward sm = new SM_Common_Identify_Treasure_Reward();
		sm.activeName = activeName;
		return sm;
	}

	public String getActiveName() {
		return activeName;
	}

	public SM_Common_Identify_Treasure_Reward setActiveName(String activeName) {
		this.activeName = activeName;
		return this;
	}

	public int getCurrentLuckValue() {
		return currentLuckValue;
	}

	public SM_Common_Identify_Treasure_Reward setCurrentLuckValue(int currentLuckValue) {
		this.currentLuckValue = currentLuckValue;
		return this;
	}

	public String getItemId() {
		return itemId;
	}

	public SM_Common_Identify_Treasure_Reward setItemId(String itemId) {
		this.itemId = itemId;
		return this;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
