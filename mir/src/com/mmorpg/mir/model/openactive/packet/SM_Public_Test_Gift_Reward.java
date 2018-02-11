package com.mmorpg.mir.model.openactive.packet;

public class SM_Public_Test_Gift_Reward {
	private int groupId;
	
	private String nextGiftId;
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getNestGiftId() {
		return nextGiftId;
	}

	public void setNestGiftId(String nestGiftId) {
		this.nextGiftId = nestGiftId;
	}

	public static SM_Public_Test_Gift_Reward valueof(int groupId, String nextGiftId){
		SM_Public_Test_Gift_Reward sr = new SM_Public_Test_Gift_Reward();
		sr.groupId = groupId;
		sr.nextGiftId = nextGiftId;
		return sr;
	}
}
