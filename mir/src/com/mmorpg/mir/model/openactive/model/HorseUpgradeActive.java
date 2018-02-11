package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class HorseUpgradeActive implements CompeteRankActivity{

	/** 开服竞技的最大坐骑品阶 */
	private transient int rankMaxGrade;
	/** 开服活动的最大坐骑品阶 */
	private int maxGrade;
	/** 开服活动的奖励集合 */
	private HashSet<String> activityRewarded;
	/** 七日竞技的奖励 */
	private transient HashSet<String> rewarded;

	public static HorseUpgradeActive valueOf() {
		HorseUpgradeActive result = new HorseUpgradeActive();
		result.rewarded = New.hashSet();
		result.activityRewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}

	public int getRankMaxGrade() {
		return rankMaxGrade;
	}

	public void setRankMaxGrade(int rankMaxGrade) {
		this.rankMaxGrade = rankMaxGrade;
	}

	@JsonIgnore
	public int getCompeteValue() {
		return rankMaxGrade;
	}

	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.HORSE_RANK.getRankTypeValue();
	}

	public HashSet<String> getActivityRewarded() {
		return activityRewarded;
	}

	public void setActivityRewarded(HashSet<String> activityRewarded) {
		this.activityRewarded = activityRewarded;
	}
	
}
