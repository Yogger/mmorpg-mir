package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class MilitaryActive implements CompeteRankActivity{
	/** 开服活动的最高军衔 */
	private int rank;
	/** 已经领取的军衔等级礼包 */
	private HashSet<String> rewarded = New.hashSet();
	
	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@JsonIgnore
	public int getCompeteValue() {
		return rank;
	}

	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.MILITARY_RANK.getRankTypeValue();
	}

}
