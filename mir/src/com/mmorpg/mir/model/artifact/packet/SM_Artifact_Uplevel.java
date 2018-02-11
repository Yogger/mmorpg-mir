package com.mmorpg.mir.model.artifact.packet;

import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.core.action.CoreActions;

/**
 * 处理进阶
 * 
 * @author 37wan
 * 
 */
public class SM_Artifact_Uplevel {

	private int code; // 错误码
	// 阶数
	private int grade;
	// 等级
	private int level;
	// 总次数
	private int totalCount;
	private long clearTime; // 清除祝福值的时间
	// 增加的次数 前端需要显示
	private int addCount;
	private CoreActions actions;

	public static SM_Artifact_Uplevel valueOf(Artifact artifact) {
		SM_Artifact_Uplevel response = new SM_Artifact_Uplevel();
		response.grade = artifact.getLevel();
		response.level = artifact.getRank();
		response.totalCount = artifact.getUpSum();
		response.clearTime = artifact.getClearTime();
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

	public CoreActions getActions() {
		return actions;
	}

	public void setActions(CoreActions actions) {
		this.actions = actions;
	}

	public int getAddCount() {
		return addCount;
	}

	public void setAddCount(int addCount) {
		this.addCount = addCount;
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

}
