package com.mmorpg.mir.model.group.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class GroupApply {
	private long playerId;
	private String name;
	private int role;
	private int level;
	private int promotionId;

	public GroupApplyVO createVO() {
		return GroupApplyVO.valueOf(this);
	}

	public static GroupApply valueOf(Player player) {
		GroupApply apply = new GroupApply();
		apply.playerId = player.getObjectId();
		apply.name = player.getName();
		apply.role = player.getPlayerEnt().getRole();
		apply.level = player.getLevel();
		apply.promotionId = player.getPromotion().getStage();
		return apply;
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
		GroupApply other = (GroupApply) obj;
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
