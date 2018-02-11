package com.mmorpg.mir.model.blackshop.packet;

import com.mmorpg.mir.model.blackshop.packet.vo.BlackShopActivityVo;

public class SM_BlackShop_Activity_Change {

	private BlackShopActivityVo activityVo;

	public static SM_BlackShop_Activity_Change valueOf() {
		SM_BlackShop_Activity_Change result = new SM_BlackShop_Activity_Change();
		result.activityVo = BlackShopActivityVo.valueOf();
		return result;
	}

	public BlackShopActivityVo getActivityVo() {
		return activityVo;
	}

	public void setActivityVo(BlackShopActivityVo activityVo) {
		this.activityVo = activityVo;
	}

}
