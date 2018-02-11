package com.mmorpg.mir.model.skill.packet;

public class SM_Skill_Common {
	private int code;

	public static SM_Skill_Common valueOf(int code) {
		SM_Skill_Common sm = new SM_Skill_Common();
		sm.code = code;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
