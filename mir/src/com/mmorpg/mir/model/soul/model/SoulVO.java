package com.mmorpg.mir.model.soul.model;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.core.SoulManager;

/**
 * 英魂
 * 
 * @author 37wan
 * 
 */
public class SoulVO {

	private boolean open; // 是否开启
	// 阶数
	private int grade;
	private int level; // 记录玩家当前已经达到的英魂等级
	private int totalCount;
	private long clearTime; // 清除祝福值的时间
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	public static SoulVO valueOf(Player player) {
		SoulVO vo = new SoulVO();
		boolean b = SoulManager.getInstance().isOpen(player);
		vo.setOpen(b);
		vo.setGrade(player.getSoul().getLevel());
		vo.setLevel(player.getSoul().getRank());
		vo.setTotalCount(player.getSoul().getUpSum());
		vo.setClearTime(player.getSoul().getClearTime());
		vo.setEnhanceItemCount(player.getSoul().getEnhanceItemCount());
		vo.setGrowItemCount(player.getSoul().getGrowItemCount());
		return vo;
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
