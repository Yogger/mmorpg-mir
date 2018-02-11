package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.windforce.common.utility.New;

public class FightPowerActive implements CompeteRankActivity {

	/** 开服活动时间的最高战斗力 */
	private int fightPower;
	/** 已经领取的等级礼包 */
	private HashSet<String> rewarded = New.hashSet();
	
	public static FightPowerActive valueOf() {
		FightPowerActive active = new FightPowerActive();
		return active;
	}
	
	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getFightPower() {
		return fightPower;
	}

	public void setFightPower(int fightPower) {
		this.fightPower = fightPower;
	}

	@JsonIgnore
	public boolean updateFightPower(BattleScoreRefreshEvent event) {
		if (fightPower < event.getBattleScore()) {
			fightPower = event.getBattleScore();
			return true;
		}
		return false;
	}

	@JsonIgnore
	public int getCompeteValue() {
		return fightPower;
	}

	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.FIGHTPOWER_RANK.getRankTypeValue();
	}
	
}
