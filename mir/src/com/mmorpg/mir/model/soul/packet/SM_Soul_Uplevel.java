package com.mmorpg.mir.model.soul.packet;

import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.soul.model.Soul;

/**
 * 处理进阶
 * 
 * @author 37wan
 * 
 */
public class SM_Soul_Uplevel {

	private int code; // 错误码
	// 阶数
	private int grade;
	// 等级
	private int level;
	// 总次数
	private int totalCount;
	// 增加的次数 前端显示
	private int addCount;
	private long clearTime; // 清除祝福值的时间
	private CoreActions actions;

	public static SM_Soul_Uplevel valueOf(Soul soul) {
		SM_Soul_Uplevel response = new SM_Soul_Uplevel();
		response.grade = soul.getLevel();
		response.level = soul.getRank();
		response.setTotalCount(soul.getUpSum());
		response.clearTime = soul.getClearTime();
		return response;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public CoreActions getActions() {
		return actions;
	}

	public void setActions(CoreActions actions) {
		this.actions = actions;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getAddCount() {
		return addCount;
	}

	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}

}
