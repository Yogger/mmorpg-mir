package com.mmorpg.mir.model.skill.packet;

public class SM_LearnNewSkill {
	private int skillId;

	public static SM_LearnNewSkill valueOf(int skillId) {
		SM_LearnNewSkill sm = new SM_LearnNewSkill();
		sm.skillId = skillId;
		return sm;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

}
