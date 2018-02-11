package com.mmorpg.mir.model.mergeactive.packet;

public class SM_Merge_Login_Reward {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static SM_Merge_Login_Reward valueOf(String id){
		SM_Merge_Login_Reward sm = new SM_Merge_Login_Reward();
		sm.id = id;
		return sm;
	}
}
