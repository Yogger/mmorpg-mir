package com.mmorpg.mir.model.soul.packet.vo;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SoulSimpleVo {
	private int grade;
	private int level;
	/** 道具数量 */
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	public static SoulSimpleVo valueOf(Player player) {
		SoulSimpleVo result = new SoulSimpleVo();
		result.grade = player.getSoul().getLevel();
		result.level = player.getSoul().getRank();
		result.enhanceItemCount = player.getSoul().getEnhanceItemCount();
		result.growItemCount = player.getSoul().getGrowItemCount();
		return result;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

}
