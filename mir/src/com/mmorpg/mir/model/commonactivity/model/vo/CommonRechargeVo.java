package com.mmorpg.mir.model.commonactivity.model.vo;

import com.mmorpg.mir.model.commonactivity.model.CommonRechargeCelebrate;

public class CommonRechargeVo {
	private String acitityName;

	private String rewardedId;

	private long rechargeAmount;

	public static CommonRechargeVo valueOf(CommonRechargeCelebrate recharge) {
		CommonRechargeVo result = new CommonRechargeVo();
		result.acitityName = recharge.getAcitityName();
		result.rewardedId = recharge.getRewardedId();
		result.rechargeAmount = recharge.getRechargeAmount();
		return result;
	}

	public String getAcitityName() {
		return acitityName;
	}

	public void setAcitityName(String acitityName) {
		this.acitityName = acitityName;
	}

	public String getRewardedId() {
		return rewardedId;
	}

	public void setRewardedId(String rewardedId) {
		this.rewardedId = rewardedId;
	}

	public long getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(long rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

}
