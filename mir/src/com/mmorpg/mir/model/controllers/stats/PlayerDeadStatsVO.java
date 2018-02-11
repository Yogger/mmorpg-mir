package com.mmorpg.mir.model.controllers.stats;

import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;


public class PlayerDeadStatsVO {

	private boolean isAlreadyDead;

	private int buyCounts;

	private long lastDeadTime;

	private long lastAttackerId;

	private String lastAttackerName;
	
	private boolean inBossRange;
	
	public static PlayerDeadStatsVO createVO(PlayerLifeStats lifeStats) {
		PlayerDeadStatsVO vo = new PlayerDeadStatsVO();
		vo.isAlreadyDead = lifeStats.isAlreadyDead();
		vo.buyCounts = lifeStats.getBuyCounts();
		vo.lastDeadTime = lifeStats.getLastDeadTime();
		vo.lastAttackerId = lifeStats.getLastAttackerId();
		vo.lastAttackerName = lifeStats.getLastAttackerName();
		vo.inBossRange = PlayerReliveManager.getInstance().isInBossRange(lifeStats.getOwner());
		return vo;
	}

	public boolean isAlreadyDead() {
		return isAlreadyDead;
	}

	public void setAlreadyDead(boolean isAlreadyDead) {
		this.isAlreadyDead = isAlreadyDead;
	}

	public int getBuyCounts() {
		return buyCounts;
	}

	public void setBuyCounts(int buyCounts) {
		this.buyCounts = buyCounts;
	}

	public long getLastDeadTime() {
		return lastDeadTime;
	}

	public void setLastDeadTime(long lastDeadTime) {
		this.lastDeadTime = lastDeadTime;
	}

	public long getLastAttackerId() {
		return lastAttackerId;
	}

	public void setLastAttackerId(long lastAttackerId) {
		this.lastAttackerId = lastAttackerId;
	}

	public String getLastAttackerName() {
		return lastAttackerName;
	}

	public void setLastAttackerName(String lastAttackerName) {
		this.lastAttackerName = lastAttackerName;
	}

	public boolean isInBossRange() {
		return inBossRange;
	}

	public void setInBossRange(boolean inBossRange) {
		this.inBossRange = inBossRange;
	}
	
}
