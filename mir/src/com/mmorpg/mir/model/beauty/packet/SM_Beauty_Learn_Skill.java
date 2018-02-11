package com.mmorpg.mir.model.beauty.packet;

public class SM_Beauty_Learn_Skill {
	private String girlId;
	private int index;
	private int skillId;
	// 主动or被动
	private boolean proactive;

	public static SM_Beauty_Learn_Skill valueOf(String girlId, boolean proactive, int index, int skillId) {
		SM_Beauty_Learn_Skill result = new SM_Beauty_Learn_Skill();
		result.girlId = girlId;
		result.proactive = proactive;
		result.index = index;
		result.skillId = skillId;
		return result;
	}

	public String getGirlId() {
		return girlId;
	}

	public int getIndex() {
		return index;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setGirlId(String girlId) {
		this.girlId = girlId;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public boolean isProactive() {
		return proactive;
	}

	public void setProactive(boolean proactive) {
		this.proactive = proactive;
	}

}
