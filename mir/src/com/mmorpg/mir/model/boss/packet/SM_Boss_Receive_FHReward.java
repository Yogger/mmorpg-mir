package com.mmorpg.mir.model.boss.packet;

public class SM_Boss_Receive_FHReward {
	private String bossId;

	public static SM_Boss_Receive_FHReward valueOf(String bossId) {
		SM_Boss_Receive_FHReward result = new SM_Boss_Receive_FHReward();
		result.bossId = bossId;
		return result;
	}

	public String getBossId() {
		return bossId;
	}

	public void setBossId(String bossId) {
		this.bossId = bossId;
	}

}
