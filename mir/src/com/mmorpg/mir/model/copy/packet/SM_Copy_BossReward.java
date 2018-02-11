package com.mmorpg.mir.model.copy.packet;

public class SM_Copy_BossReward {

	private String id;

	public static SM_Copy_BossReward valueOf(String i) {
		SM_Copy_BossReward sm = new SM_Copy_BossReward();
		sm.id = i;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
