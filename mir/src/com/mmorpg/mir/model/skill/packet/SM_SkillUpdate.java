package com.mmorpg.mir.model.skill.packet;

import com.mmorpg.mir.model.skill.model.SkillEntry;

public class SM_SkillUpdate {
	private int skillId;
	private int exp;
	private int level;
	private boolean end;
	private byte none;

	public static SM_SkillUpdate valueOf(SkillEntry se) {
		SM_SkillUpdate sm = new SM_SkillUpdate();
		sm.skillId = se.getId();
		sm.exp = se.getExp();
		sm.level = se.getLevel();
		sm.end = se.isEnd();
		return sm;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public byte getNone() {
		return none;
	}

	public void setNone(byte none) {
		this.none = none;
	}

}
