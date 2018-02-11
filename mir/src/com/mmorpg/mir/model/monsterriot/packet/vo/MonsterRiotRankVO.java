package com.mmorpg.mir.model.monsterriot.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public class MonsterRiotRankVO implements Comparable<MonsterRiotRankVO>{
	private long playerId;
	private int rank;
	private String name;
	private byte role;
	private byte promotionId;
	private int level;
	private long damage;
	private Reward reward;

	public static MonsterRiotRankVO valueof(int rank, long damage, Player player, Reward reward) {
		MonsterRiotRankVO vo = new MonsterRiotRankVO();
		vo.name = player.getName();
		vo.rank = rank;
		vo.reward = reward;
		vo.level = player.getLevel();
		vo.playerId = player.getObjectId();
		vo.role = (byte) player.getRole();
		vo.promotionId = (byte) player.getPromotion().getStage();
		vo.damage = damage;
		return vo;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(byte promotionId) {
		this.promotionId = promotionId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	public int compareTo(MonsterRiotRankVO o) {
		return rank - o.getRank();
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

}
