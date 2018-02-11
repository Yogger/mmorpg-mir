package com.mmorpg.mir.model.artifact.model;

import java.util.Map;

import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 神兵
 * 
 * @author 37wan
 * 
 */
public class ArtifactVO {

	private boolean open; // 是否开启
	// 阶数
	private int grade;
	// 等级
	private int level;
	private int totalCount;
	private long clearTime; // 清除祝福值的时间
	private int appLevel;
	private long buffEndTime;
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	public static ArtifactVO valueOf(Player player) {
		ArtifactVO vo = new ArtifactVO();
		vo.setOpen(ArtifactManager.getInstance().isOpen(player));
		vo.setClearTime(player.getArtifact().getClearTime());
		vo.setGrade(player.getArtifact().getLevel());
		vo.setLevel(player.getArtifact().getRank());
		vo.setAppLevel(player.getArtifact().getAppLevel());
		vo.setTotalCount(player.getArtifact().getUpSum());
		vo.setBuffEndTime(player.getArtifact().getBuffEndTime());
		vo.setEnhanceItemCount(player.getArtifact().getEnhanceItemCount());
		vo.setGrowItemCount(player.getArtifact().getGrowItemCount());
		return vo;
		// return ValueOfUtil.valueOf(ArtifactVO.class, artifact);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAppLevel() {
		return appLevel;
	}

	public void setAppLevel(int appLevel) {
		this.appLevel = appLevel;
	}

	public long getBuffEndTime() {
		return buffEndTime;
	}

	public void setBuffEndTime(long buffEndTime) {
		this.buffEndTime = buffEndTime;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
