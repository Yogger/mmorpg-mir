package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class CountryHeroActive implements CompeteRankActivity {
	/** 开服竞技的最大杀人数量 */
	private transient int killNums;
	/** 七日竞技的奖励 */
	private transient HashSet<String> rewarded;
	
	public static CountryHeroActive valueOf() {
		CountryHeroActive heroActive = new CountryHeroActive();
		heroActive.rewarded = New.hashSet();
		return heroActive;
	}

	@Override
	@JsonIgnore
	public int getCompeteValue() {
		return killNums;
	}

	@Override
	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.ACTIVITY_COUNTRY_HERO.getRankTypeValue();
	}

	@Override
	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public int getKillNums() {
		return killNums;
	}

	public void setKillNums(int killNums) {
		this.killNums = killNums;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	@JsonIgnore
	public int addKillNums(int i) {
		killNums++;
		return killNums;
	}
	
}
