package com.mmorpg.mir.model.gameobjects.event;

import com.windforce.common.event.event.IEvent;

public class BattleScoreRefreshEvent implements IEvent {

	private long playerId;
	private int battleScore;
	private int level;
	
	private boolean becomeMorePower;

	@Override
	public long getOwner() {
		return playerId;
	}

	public static BattleScoreRefreshEvent valueOf(long pid, int level, int score, boolean morePower) {
		BattleScoreRefreshEvent e = new BattleScoreRefreshEvent();
		e.playerId = pid;
		e.battleScore = score;
		e.level = level;
		e.becomeMorePower = morePower;
		return e;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getBattleScore() {
		return battleScore;
	}

	public void setBattleScore(int battleScore) {
		this.battleScore = battleScore;
	}

	public int getLevel() {
    	return level;
    }

	public void setLevel(int level) {
    	this.level = level;
    }

	public boolean isBecomeMorePower() {
		return becomeMorePower;
	}

	public void setBecomeMorePower(boolean becomeMorePower) {
		this.becomeMorePower = becomeMorePower;
	}

}
