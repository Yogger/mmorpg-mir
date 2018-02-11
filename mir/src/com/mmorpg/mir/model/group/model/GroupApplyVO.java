package com.mmorpg.mir.model.group.model;

public class GroupApplyVO {
	private long playerId;
	private String name;
	private int role;
	private int level;
	private int promotionId;

	public static GroupApplyVO valueOf(GroupApply apply) {
		GroupApplyVO vo = new GroupApplyVO();
		vo.playerId = apply.getPlayerId();
		vo.name = apply.getName();
		vo.role = apply.getRole();
		vo.level = apply.getLevel();
		vo.promotionId = apply.getPromotionId();
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
		GroupApplyVO other = (GroupApplyVO) obj;
		if (playerId != other.playerId)
			return false;
		return true;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
