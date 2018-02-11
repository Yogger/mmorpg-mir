package com.mmorpg.mir.model.monsterriot.packet;

import java.util.Map;

import com.mmorpg.mir.model.monsterriot.packet.vo.MonsterRiotRankVO;
import com.mmorpg.mir.model.reward.model.Reward;

public class SM_MonsterRiot_Reward {
	private Map<Integer, MonsterRiotRankVO> rankElements;
	private int rank;
	private int round;
	private Reward reward;

	public static SM_MonsterRiot_Reward valueOf(int rank, int round, Reward reward, Map<Integer, MonsterRiotRankVO> ranks) {
		SM_MonsterRiot_Reward sm = new SM_MonsterRiot_Reward();
		sm.rank = rank;
		sm.round = round;
		sm.reward = reward;
		sm.rankElements = ranks;
		return sm;
	}

	public Map<Integer, MonsterRiotRankVO> getRankElements() {
		return rankElements;
	}

	public void setRankElements(Map<Integer, MonsterRiotRankVO> rankElements) {
		this.rankElements = rankElements;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
