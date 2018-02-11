package com.mmorpg.mir.model.seal.packet;


public class SM_Seal_Learn_Skill {

	private int index;

	private int skillId;

	public static SM_Seal_Learn_Skill valueOf(int index, int skillId) {
		SM_Seal_Learn_Skill result = new SM_Seal_Learn_Skill();
		result.index = index;
		result.skillId = skillId;
		return result;
	}

	public int getIndex() {
		return index;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

}
