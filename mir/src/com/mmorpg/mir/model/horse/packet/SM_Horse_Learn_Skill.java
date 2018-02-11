package com.mmorpg.mir.model.horse.packet;

public class SM_Horse_Learn_Skill {
	private int index;
	
	private int skillId;
	
	public static SM_Horse_Learn_Skill valueOf(int i, int skill) {
		SM_Horse_Learn_Skill sm = new SM_Horse_Learn_Skill();
		sm.index = i;
		sm.skillId = skill;
		return sm;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
}
