package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.boss.model.BossGiftInfo;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.welfare.packet.vo.BossGiftVO;

public class SM_Query_Gift {
	private ArrayList<BossGiftVO> giftRank;
	private String spawnKey;
	private PlayerSimpleInfo lastAttacker;
	private int selfGift;
	
	public static SM_Query_Gift valueOf(PlayerSimpleInfo lastAttack, BossGiftInfo info, String spawnKey, int giftAmount) {
		SM_Query_Gift sm = new SM_Query_Gift();
		sm.giftRank = info.getTop10();
		sm.spawnKey = spawnKey;
		sm.lastAttacker = lastAttack;
		sm.selfGift = giftAmount;
		return sm;
	}

	public ArrayList<BossGiftVO> getGiftRank() {
		return giftRank;
	}

	public void setGiftRank(ArrayList<BossGiftVO> giftRank) {
		this.giftRank = giftRank;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public PlayerSimpleInfo getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(PlayerSimpleInfo lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	public int getSelfGift() {
		return selfGift;
	}

	public void setSelfGift(int selfGift) {
		this.selfGift = selfGift;
	}

}
