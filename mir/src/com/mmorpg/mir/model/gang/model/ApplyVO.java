package com.mmorpg.mir.model.gang.model;

public class ApplyVO {
	private long playerId;
	private String name;
	private int role;
	private int level;
	private int battleScore;

	public static ApplyVO valueOf(Apply apply) {
		ApplyVO vo = new ApplyVO();
		vo.playerId = apply.getPlayerId();
		vo.name = apply.getName();
		vo.role = apply.getRole();
		vo.level = apply.getLevel();
		vo.battleScore = apply.getBattleScore();
		return vo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (playerId ^ (playerId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplyVO other = (ApplyVO) obj;
		if (playerId != other.playerId)
			return false;
		return true;
	}

	public int getBattleScore() {
    	return battleScore;
    }

	public void setBattleScore(int battleScore) {
    	this.battleScore = battleScore;
    }

}
