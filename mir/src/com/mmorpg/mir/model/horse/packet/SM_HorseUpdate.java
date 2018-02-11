package com.mmorpg.mir.model.horse.packet;

import java.util.Map;

import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.horse.model.HorseAppearance;

public class SM_HorseUpdate {
	private int code;
	private int grade;
	private int level;
	private int totalCount;
	private long clearTime;
	private HorseAppearance appearance;
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;
	// 用来显示暴击活动前端显示
	private int addCount;
	private CoreActions actions;

	public int getGrade() {
		return grade;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CoreActions getActions() {
		return actions;
	}

	public void setActions(CoreActions actions) {
		this.actions = actions;
	}

	public HorseAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(HorseAppearance appearance) {
		this.appearance = appearance;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAddCount() {
		return addCount;
	}

	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

}
