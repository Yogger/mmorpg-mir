package com.mmorpg.mir.model.commonactivity.packet;

public class CM_CommonMarcoShop_Buy {

	private String activityName;
	private int gridIndex;

	public int getGridIndex() {
		return gridIndex;
	}

	public void setGridIndex(int gridIndex) {
		this.gridIndex = gridIndex;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
