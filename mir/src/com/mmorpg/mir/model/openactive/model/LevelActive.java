package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class LevelActive implements CompeteRankActivity{
	/** 开服活动的最高等级 */
	private int level;
	/** 已经领取的等级礼包 */
	private HashSet<String> rewarded = New.hashSet();

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@JsonIgnore
	public int getCompeteValue() {
		return level;
	}

	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.LEVEL_RANK.getRankTypeValue();
	}

}
