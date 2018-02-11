package com.mmorpg.mir.model.countrycopy.model.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public class TechCopyRankVO implements Comparable<TechCopyRankVO>{
	/** 排名 */
	private int rank;
	/** 名字 */
	private String name;
	/** 伤害 */
	private long damage;
	/** 角色 */
	private byte role;
	/** 转职 */
	private byte promotionId;
	/** 等级 */
	private int level;
	/** 奖励 */
	private Reward reward;

	public static TechCopyRankVO valueOf(int rank, Long damage, Player player, Reward r) {
		TechCopyRankVO vo = new TechCopyRankVO();
		vo.rank = rank;
		vo.damage = (damage == null ? 0L : damage);
		vo.name = player.getName();
		vo.role = (byte) player.getRole();
		vo.promotionId = (byte) player.getPromotion().getStage();
		vo.reward = r;
		vo.level = player.getLevel();
		return vo;
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

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
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

	@Override
	public int compareTo(TechCopyRankVO o) {
		return o.rank - rank;
	}

}
