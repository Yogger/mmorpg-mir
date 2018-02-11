package com.mmorpg.mir.model.warbook.packet.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.warbook.model.Warbook;

public class WarbookSimpleVo {
	private int grade;
	private int level;
	private HashMap<String, Integer> itemCount;

	public static WarbookSimpleVo valueOf(Warbook warbook) {
		WarbookSimpleVo result = new WarbookSimpleVo();
		result.grade = warbook.getGrade();
		result.level = warbook.getLevel();
		result.itemCount = new HashMap<String, Integer>(warbook.getItemCount());
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

	public HashMap<String, Integer> getItemCount() {
		return itemCount;
	}

	public void setItemCount(HashMap<String, Integer> itemCount) {
		this.itemCount = itemCount;
	}

}
