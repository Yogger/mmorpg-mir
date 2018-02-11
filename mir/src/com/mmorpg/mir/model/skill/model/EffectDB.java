package com.mmorpg.mir.model.skill.model;

public class EffectDB {
	private int skillId;
	private long saveTime;
	private int skillLevel;
	private long endTime;
	private long reserved3;

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getReserved3() {
		return reserved3;
	}

	public void setReserved3(long reserved3) {
		this.reserved3 = reserved3;
	}

}
