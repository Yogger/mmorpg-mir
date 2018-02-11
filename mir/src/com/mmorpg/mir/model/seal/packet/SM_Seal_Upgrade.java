package com.mmorpg.mir.model.seal.packet;

import com.mmorpg.mir.model.seal.model.Seal;

public class SM_Seal_Upgrade {
	
	private int grade;
	private int level;
	private int upGradeCount;
	// 前端显示暴击
	private int countAdd;
	private long clearTime;

	private int code;

	public static SM_Seal_Upgrade valueOf(Seal seal, int countAdd) {
		SM_Seal_Upgrade result = new SM_Seal_Upgrade();
		result.grade = seal.getGrade();
		result.level = seal.getLevel();
		result.upGradeCount = seal.getUpCount();
		result.clearTime = seal.getClearTime();
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
