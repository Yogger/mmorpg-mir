package com.mmorpg.mir.model.skill.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

/**
 * 通知玩家使用技能
 * 
 * @author liuzhou
 * 
 */
public class SM_UseSkill_Start {
	/** 技能使用者 */
	private long effector;

	private short skillId;
	private long targetId;

	public long getEffector() {
		return effector;
	}

	public void setEffector(long effector) {
		this.effector = effector;
	}

	public short getSkillId() {
		return skillId;
	}

	public void setSkillId(short skillId) {
		this.skillId = skillId;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public static SM_UseSkill_Start valueOf(Creature creature, int skillId, long targetId) {
		SM_UseSkill_Start result = new SM_UseSkill_Start();
		result.effector = creature.getObjectId();
		result.skillId = (short) skillId;
		result.targetId = targetId;
		return result;
	}
}
