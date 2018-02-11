package com.mmorpg.mir.model.fashion.packet.vo;

import java.util.HashSet;

import com.mmorpg.mir.model.fashion.model.FashionPool;

public class FashionPoolVo {
	/** 等级 */
	private int level;
	/** 经验 */
	private int exp;
	/** 是否隐藏模型 */
	private boolean hided;
	/** 拥有的时装 */
	private HashSet<Integer> ownFashions;
	/** 当前正在穿 */
	private int currentFashionId;

	public static FashionPoolVo valueOf(FashionPool pool) {
		FashionPoolVo result = new FashionPoolVo();
		result.level = pool.getLevel();
		result.exp = pool.getExp();
		result.hided = pool.isHided();
		result.ownFashions = new HashSet<Integer>(pool.getOwnFashions());
		result.currentFashionId = pool.getCurrentFashionId();
		return result;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public boolean isHided() {
		return hided;
	}

	public void setHided(boolean hided) {
		this.hided = hided;
	}

	public HashSet<Integer> getOwnFashions() {
		return ownFashions;
	}

	public void setOwnFashions(HashSet<Integer> ownFashions) {
		this.ownFashions = ownFashions;
	}

	public int getCurrentFashionId() {
		return currentFashionId;
	}

	public void setCurrentFashionId(int currentFashionId) {
		this.currentFashionId = currentFashionId;
	}

}
