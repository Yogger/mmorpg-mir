package com.mmorpg.mir.model.skill.packet;

/**
 * 通知玩家使用技能
 * 
 * @author liuzhou
 * 
 */
public class SM_UseSkill {
	private int code;
	private int skillId;
	private long nextTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
}
