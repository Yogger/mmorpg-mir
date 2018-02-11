package com.mmorpg.mir.model.openactive.packet;

public class SM_Draw_OldCompeteReward {
	private int rankType;

	private String resourceId;

	public static SM_Draw_OldCompeteReward valueOf(int t, String resourceId) {
		SM_Draw_OldCompeteReward sm = new SM_Draw_OldCompeteReward();
		sm.rankType = t;
		sm.resourceId = resourceId;
		return sm;
	}

	public int getRankType() {
		return rankType;
	}

	public void setRankType(int rankType) {
		this.rankType = rankType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
