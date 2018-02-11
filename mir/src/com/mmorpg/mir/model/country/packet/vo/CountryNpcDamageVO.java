package com.mmorpg.mir.model.country.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public class CountryNpcDamageVO {

	private long playerId;
	private String name;
	private long damage;
	private int rank;
	private int level;
	private byte country;
	private byte role;
	private byte promotionId;
	private Reward reward;
	
	public static CountryNpcDamageVO valueOf(int rank, Player player, Reward reward, Long damage) {
		CountryNpcDamageVO vo = new CountryNpcDamageVO();
		vo.name = player.getName();
		vo.rank = rank;
		vo.country = (byte) player.getCountryValue();
		vo.reward = reward;
		vo.damage = damage == null? 0L: damage.longValue();
		vo.level = player.getLevel();
		vo.playerId = player.getObjectId();
		vo.role = (byte) player.getRole();
		vo.promotionId = (byte) player.getPromotion().getStage();
		return vo;
	}
	
	public static CountryNpcDamageVO valueOf(int rank, Player player, long damage) {
		return CountryNpcDamageVO.valueOf(rank, player, null, damage);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
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

}
