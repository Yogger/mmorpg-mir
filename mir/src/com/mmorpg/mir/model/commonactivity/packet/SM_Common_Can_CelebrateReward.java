package com.mmorpg.mir.model.commonactivity.packet;

import com.mmorpg.mir.model.commonactivity.model.CommonRechargeCelebrate;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonRechargeVo;

public class SM_Common_Can_CelebrateReward {

	private CommonRechargeVo commonRechargeVo;

	public static SM_Common_Can_CelebrateReward valueOf(CommonRechargeCelebrate rechargeCelebrate) {
		SM_Common_Can_CelebrateReward result = new SM_Common_Can_CelebrateReward();
		result.commonRechargeVo = CommonRechargeVo.valueOf(rechargeCelebrate);
		return result;
	}

	public CommonRechargeVo getCommonRechargeVo() {
		return commonRechargeVo;
	}

	public void setCommonRechargeVo(CommonRechargeVo commonRechargeVo) {
		this.commonRechargeVo = commonRechargeVo;
	}

}
