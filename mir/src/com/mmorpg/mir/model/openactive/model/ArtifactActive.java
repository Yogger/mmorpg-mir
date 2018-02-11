package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class ArtifactActive implements CompeteRankActivity{
	
	/** 开服竞技的最大神兵品阶 */
	private transient int rankMaxGrade;
	/** 开服活动的最大神兵品阶 */
	private int maxGrade;
	/** 开服活动的奖励集合 */
	private HashSet<String> activityRewarded;
	/** 已经领取的经验礼包 */
	private transient HashSet<String> rewarded;

	public static ArtifactActive valueOf() {
		ArtifactActive result = new ArtifactActive();
		result.rewarded = New.hashSet();
		result.activityRewarded = New.hashSet();
		return result;
	}

	@Override
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
		return CompeteRankValue.ARTIFACT_RANK.getRankTypeValue();
	}

	public HashSet<String> getActivityRewarded() {
		return activityRewarded;
	}

	public void setActivityRewarded(HashSet<String> activityRewarded) {
		this.activityRewarded = activityRewarded;
	}
	
}
