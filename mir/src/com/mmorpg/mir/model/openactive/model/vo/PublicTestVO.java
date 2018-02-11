package com.mmorpg.mir.model.openactive.model.vo;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseThreeInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseTwoInfo;

public class PublicTestVO {

	private boolean bossAlive;

	private SM_GroupPurchaseInfo groupPurchaseInfo;

	private SM_GroupPurchaseTwoInfo groupPurchaseTwoInfo;

	private SM_GroupPurchaseThreeInfo groupPurchaseThreeInfo;

	private Map<Integer, String> frontShowGift;

	public static PublicTestVO valueOf(Player player) {
		PublicTestVO vo = new PublicTestVO();
		vo.bossAlive = OpenActiveManager.getInstance().specialBossIsAlive(player.getCountryId());
		vo.groupPurchaseInfo = SM_GroupPurchaseInfo.valueOf(player);
		vo.groupPurchaseTwoInfo = SM_GroupPurchaseTwoInfo.valueOf(player);
		vo.groupPurchaseThreeInfo = SM_GroupPurchaseThreeInfo.valueOf(player);
		vo.frontShowGift = player.getOpenActive().getGiftActive().getFrontShowGift(player);
		return vo;
	}

	public boolean isBossAlive() {
		return bossAlive;
	}

	public void setBossAlive(boolean bossAlive) {
		this.bossAlive = bossAlive;
	}

	public SM_GroupPurchaseInfo getGroupPurchaseInfo() {
		return groupPurchaseInfo;
	}

	public void setGroupPurchaseInfo(SM_GroupPurchaseInfo groupPurchaseInfo) {
		this.groupPurchaseInfo = groupPurchaseInfo;
	}

	public Map<Integer, String> getFrontShowGift() {
		return frontShowGift;
	}

	public void setFrontShowGift(Map<Integer, String> frontShowGift) {
		this.frontShowGift = frontShowGift;
	}

	public SM_GroupPurchaseTwoInfo getGroupPurchaseTwoInfo() {
		return groupPurchaseTwoInfo;
	}

	public void setGroupPurchaseTwoInfo(SM_GroupPurchaseTwoInfo groupPurchaseTwoInfo) {
		this.groupPurchaseTwoInfo = groupPurchaseTwoInfo;
	}

	public SM_GroupPurchaseThreeInfo getGroupPurchaseThreeInfo() {
		return groupPurchaseThreeInfo;
	}

	public void setGroupPurchaseThreeInfo(SM_GroupPurchaseThreeInfo groupPurchaseThreeInfo) {
		this.groupPurchaseThreeInfo = groupPurchaseThreeInfo;
	}

}
