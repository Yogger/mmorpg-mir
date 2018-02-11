package com.mmorpg.mir.model.artifact.packet.vo;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class ArtifactSimpleVo {
	private int grade;
	private int level;
	/** 道具数量 */
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	public static ArtifactSimpleVo valueOf(Player player) {
		ArtifactSimpleVo result = new ArtifactSimpleVo();
		result.grade = player.getArtifact().getLevel();
		result.level = player.getArtifact().getRank();
		result.enhanceItemCount = player.getArtifact().getEnhanceItemCount();
		result.growItemCount = player.getArtifact().getGrowItemCount();
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
