package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class SoulActive implements CompeteRankActivity{
	
	/** 开服竞技的最大英魂品阶 */
	private transient int rankMaxGrade;
	/** 开服活动的最大英魂品阶 */
	private int maxGrade;
	/** 开服活动的奖励集合 */
	private HashSet<String> activityRewarded;
	/** 七日竞技的奖励 */
	private transient HashSet<String> rewarded;
	
	public static SoulActive valueOf() {
		SoulActive soulActive = new SoulActive();
		soulActive.activityRewarded = New.hashSet();
		soulActive.rewarded = New.hashSet();
		return soulActive;
	}

	@Override
	@JsonIgnore
	public int getCompeteValue() {
		return rankMaxGrade;
	}

	@Override
	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.SOUL_RANK.getRankTypeValue();
	}

	@Override
	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public int getRankMaxGrade() {
		return rankMaxGrade;
	}

	public void setRankMaxGrade(int rankMaxGrade) {
		this.rankMaxGrade = rankMaxGrade;
	}

	public int getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}

	public HashSet<String> getActivityRewarded() {
		return activityRewarded;
	}

	public void setActivityRewarded(HashSet<String> activityRewarded) {
		this.activityRewarded = activityRewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

}
