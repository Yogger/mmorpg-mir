package com.mmorpg.mir.model.boss.model;

import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.welfare.model.BossGift;
import com.mmorpg.mir.model.welfare.packet.vo.BossGiftVO;

public class BossGiftInfo {

	private String key;
	
	private ArrayList<BossGift> rankElements;
	
	@Transient
	private ArrayList<BossGiftVO> bossRankVOS;
	
	private PlayerSimpleInfo lastAttacker;

	public static BossGiftInfo valueOf(String spawnKey, PlayerSimpleInfo attacker) {
		BossGiftInfo info = new BossGiftInfo();
		info.key = spawnKey;
		info.rankElements = new ArrayList<BossGift>();
		info.lastAttacker = attacker;
		return info;
	}

	public ArrayList<BossGift> getRankElements() {
		return rankElements;
	}

	public void setRankElements(ArrayList<BossGift> rankElements) {
		this.rankElements = rankElements;
	}

	@JsonIgnore
	public ArrayList<BossGiftVO> getTop10() {
		if (bossRankVOS == null) { 
			ArrayList<BossGiftVO> rank = new ArrayList<BossGiftVO>();
			Collections.sort(rankElements);
			for (int i = 0; i < rankElements.size() && i < BossConfig.GIFT_BAG_MAX_SIZE; i++) {
				rank.add(rankElements.get(i).createVO());
			}
			bossRankVOS = rank;
		}
		return bossRankVOS;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public PlayerSimpleInfo getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(PlayerSimpleInfo lastAttacker) {
		this.lastAttacker = lastAttacker;
	}
	
}
