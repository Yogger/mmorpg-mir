package com.mmorpg.mir.model.skill.packet;

/**
 * 
 * @author Kuang Hao
 * @since v1.0 2014-10-14
 * 
 */
public class SM_DashSkill {
	private long objectId;
	private int skillId;
	private int x;
	private int y;

	public static SM_DashSkill valueOf(int skillId, int x, int y, long objectId) {
		SM_DashSkill smp = new SM_DashSkill();
		smp.skillId = skillId;
		smp.x = x;
		smp.y = y;
		smp.objectId = objectId;
		return smp;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

}
