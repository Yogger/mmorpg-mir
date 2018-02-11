package com.mmorpg.mir.model.warbook.packet;

import com.mmorpg.mir.model.warbook.model.Warbook;

public class SM_Warbook_Upgrade {
	private int grade;
	private int level;
	private int upGradeCount;
	// 前端显示暴击
	private int countAdd;
	private long clearTime;

	private int code;

	public static SM_Warbook_Upgrade valueOf(Warbook book, int countAdd) {
		SM_Warbook_Upgrade result = new SM_Warbook_Upgrade();
		result.grade = book.getGrade();
		result.level = book.getLevel();
		result.upGradeCount = book.getUpCount();
		result.clearTime = book.getClearTime();
		result.countAdd = countAdd;
		return result;
	}

	public int getGrade() {
		return grade;
	}

	public int getUpGradeCount() {
		return upGradeCount;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setUpGradeCount(int upGradeCount) {
		this.upGradeCount = upGradeCount;
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

	public int getCountAdd() {
		return countAdd;
	}

	public void setCountAdd(int countAdd) {
		this.countAdd = countAdd;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
