package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.welfare.model.BossGift;

public class SM_RedGift_Deprecate {
	private ArrayList<BossGift> leftBossGifts;

	public static SM_RedGift_Deprecate valueOf(ArrayList<BossGift> gifts) {
		SM_RedGift_Deprecate sm = new SM_RedGift_Deprecate();
		sm.leftBossGifts = new ArrayList<BossGift>(gifts);
		return sm;
	}

	public ArrayList<BossGift> getLeftBossGifts() {
		return leftBossGifts;
	}

	public void setLeftBossGifts(ArrayList<BossGift> leftBossGifts) {
		this.leftBossGifts = leftBossGifts;
	}

}
