package com.mmorpg.mir.model.blackshop.packet;

import com.mmorpg.mir.model.blackshop.model.BlackShopGood;

public class SM_BlackShop_Buy {

	private int gridIndex;

	private BlackShopGood gridGood;

	public static SM_BlackShop_Buy valueOf(int gridIndex, BlackShopGood gridGood) {
		SM_BlackShop_Buy result = new SM_BlackShop_Buy();
		result.gridIndex = gridIndex;
		result.gridGood = gridGood;
		return result;
	}

	public BlackShopGood getGridGood() {
		return gridGood;
	}

	public void setGridGood(BlackShopGood gridGood) {
		this.gridGood = gridGood;
	}

	public int getGridIndex() {
		return gridIndex;
	}

	public void setGridIndex(int gridIndex) {
		this.gridIndex = gridIndex;
	}

}
