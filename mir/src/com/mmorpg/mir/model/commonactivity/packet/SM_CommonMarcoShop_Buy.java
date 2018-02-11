package com.mmorpg.mir.model.commonactivity.packet;

import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShopGood;

public class SM_CommonMarcoShop_Buy {
	private int gridIndex;

	/** 活动名字 */
	private String activityName;

	private CommonMarcoShopGood gridGood;

	public static SM_CommonMarcoShop_Buy valueOf(int gridIndex, String activityName, CommonMarcoShopGood gridGood) {
		SM_CommonMarcoShop_Buy result = new SM_CommonMarcoShop_Buy();
		result.activityName = activityName;
		result.gridIndex = gridIndex;
		result.gridGood = gridGood;
		return result;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getGridIndex() {
		return gridIndex;
	}

	public CommonMarcoShopGood getGridGood() {
		return gridGood;
	}

	public void setGridIndex(int gridIndex) {
		this.gridIndex = gridIndex;
	}

	public void setGridGood(CommonMarcoShopGood gridGood) {
		this.gridGood = gridGood;
	}

}
