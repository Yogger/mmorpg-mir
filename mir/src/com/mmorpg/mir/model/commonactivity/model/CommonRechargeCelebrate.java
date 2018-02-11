package com.mmorpg.mir.model.commonactivity.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Can_CelebrateReward;
import com.mmorpg.mir.model.commonactivity.resource.CommonRechargeActiveResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class CommonRechargeCelebrate {

	private String acitityName;

	private String rewardedId;

	private long rechargeAmount;

	public static CommonRechargeCelebrate valueOf(String acitityName) {
		CommonRechargeCelebrate activity = new CommonRechargeCelebrate();
		activity.acitityName = acitityName;
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

	public String getAcitityName() {
		return acitityName;
	}

	public void setAcitityName(String acitityName) {
		this.acitityName = acitityName;
	}

	@JsonIgnore
	public boolean isRechargeEnough() {
		return rechargeAmount >= CommonActivityConfig.getInstance().rechargeStorages.getUnique(
				CommonRechargeActiveResource.ACTIVE_NAME_INDEX, this.acitityName).getMiniGoldRequired();
	}

	@JsonIgnore
	public void addRechargeAmount(Player player, long gold) {
		long old = rechargeAmount;
		rechargeAmount += gold;

		CommonRechargeActiveResource resource = CommonActivityConfig.getInstance().rechargeStorages.getUnique(
				CommonRechargeActiveResource.ACTIVE_NAME_INDEX, this.acitityName);
		if (rewardedId != null && rewardedId.equals(resource.getRewardGroupId())) {
			return;
		}
		if (rechargeAmount < resource.getMiniGoldRequired()) {
			return;
		}

		if (old < resource.getMiniGoldRequired()) {
			PacketSendUtility.sendPacket(player, SM_Common_Can_CelebrateReward.valueOf(this));
		}
	}

}
