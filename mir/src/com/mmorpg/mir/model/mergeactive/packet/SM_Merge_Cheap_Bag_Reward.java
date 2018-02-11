package com.mmorpg.mir.model.mergeactive.packet;


public class SM_Merge_Cheap_Bag_Reward {
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

	public static SM_Merge_Cheap_Bag_Reward valueof(int groupId, String nextGiftId){
		SM_Merge_Cheap_Bag_Reward sr = new SM_Merge_Cheap_Bag_Reward();
		sr.groupId = groupId;
		sr.nextGiftId = nextGiftId;
		return sr;
	}
}
