package com.mmorpg.mir.model.copy.packet;

public class SM_Copy_LadderReward {
	private String id;

	public static SM_Copy_LadderReward valueOf(String id) {
		SM_Copy_LadderReward sm = new SM_Copy_LadderReward();
		sm.id = id;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
