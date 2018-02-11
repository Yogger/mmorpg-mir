package com.mmorpg.mir.model.controllers.stats;

public class LifeStatDB {
	private long hp;
	private long mp;
	private long dp;
	private boolean die;
	private long lastDeadTime;
	private long buyLiveTime;
	private int buyCounts;
	private long attackerId;
	private String lastAttacker;
	private long barrier;

	public LifeStatDB() {
	}

	public LifeStatDB(long hp, long mp, boolean die, long dp, long lastDeadTime, long buyLiveTime, int buyTimes,
			String lastAttackName, long attackId, long barrier) {
		this.hp = hp;
		this.mp = mp;
		this.dp = dp;
		this.die = die;
		this.lastDeadTime = lastDeadTime;
		this.buyLiveTime = buyLiveTime;
		this.buyCounts = buyTimes;
		this.attackerId = attackId;
		this.lastAttacker = lastAttackName;
		this.barrier = barrier;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public long getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

	public boolean isDie() {
		return die;
	}

	public void setDie(boolean die) {
		this.die = die;
	}

	public long getDp() {
		return dp;
	}

	public void setDp(long dp) {
		this.dp = dp;
	}

	public long getLastDeadTime() {
		return lastDeadTime;
	}

	public void setLastDeadTime(long lastDeadTime) {
		this.lastDeadTime = lastDeadTime;
	}

	public long getBuyLiveTime() {
		return buyLiveTime;
	}

	public void setBuyLiveTime(long buyLiveTime) {
		this.buyLiveTime = buyLiveTime;
	}

	public int getBuyCounts() {
		return buyCounts;
	}

	public void setBuyCounts(int buyCounts) {
		this.buyCounts = buyCounts;
	}

	public long getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(long attackerId) {
		this.attackerId = attackerId;
	}

	public String getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(String lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	public long getBarrier() {
		return barrier;
	}

	public void setBarrier(long barrier) {
		this.barrier = barrier;
	}

}
