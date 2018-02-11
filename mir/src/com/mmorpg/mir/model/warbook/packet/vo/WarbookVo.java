package com.mmorpg.mir.model.warbook.packet.vo;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.warbook.model.Warbook;

public class WarbookVo {
	private boolean hided;
	private int grade;
	private int level;
	private int upCount;
	private long clearTime;
	private HashMap<Integer, Integer> learnSkill;
	private HashMap<String, Integer> itemCount;

	public static WarbookVo valueOf(Warbook warbook) {
		WarbookVo result = new WarbookVo();
		result.hided = warbook.isHided();
		result.grade = warbook.getGrade();
		result.level = warbook.getLevel();
		result.upCount = warbook.getUpCount();
		result.clearTime = warbook.getClearTime();
		result.learnSkill = new HashMap<Integer, Integer>(warbook.getLearnSkill());
		result.itemCount = new HashMap<String, Integer>(warbook.getItemCount());
		return result;
	}

	public int getGrade() {
		return grade;
	}

	public int getUpCount() {
		return upCount;
	}

	public Map<Integer, Integer> getLearnSkill() {
		return learnSkill;
	}

	public Map<String, Integer> getItemCount() {
		return itemCount;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}

	public void setLearnSkill(HashMap<Integer, Integer> learnSkill) {
		this.learnSkill = learnSkill;
	}

	public void setItemCount(HashMap<String, Integer> itemCount) {
		this.itemCount = itemCount;
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

	public boolean isHided() {
		return hided;
	}

	public void setHided(boolean hided) {
		this.hided = hided;
	}

}
