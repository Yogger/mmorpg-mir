package com.mmorpg.mir.model.openactive.model;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.packet.SM_Can_CelebrateReward;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class RechargeCelebrate {

	private String rewardedId;

	private long rechargeAmount;
	
	public static RechargeCelebrate valueOf() {
		RechargeCelebrate activity = new RechargeCelebrate();
		return activity;
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

	public void addRechargeAmount(Player player, long gold) {
		long old = rechargeAmount;
		rechargeAmount += gold;

		if (rewardedId != null && rewardedId.equals(OpenActiveConfig.getInstance().RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue())) {
			return;
		}
		if (rechargeAmount < OpenActiveConfig.getInstance().RECHARGE_MINIMAL_GOLD_REQUIRED.getValue()) {
			return;
		}
		if (old < OpenActiveConfig.getInstance().RECHARGE_MINIMAL_GOLD_REQUIRED.getValue()) {
			PacketSendUtility.sendPacket(player, SM_Can_CelebrateReward.valueOf(this));
		}
	}

}
