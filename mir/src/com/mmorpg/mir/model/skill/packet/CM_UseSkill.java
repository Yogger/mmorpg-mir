package com.mmorpg.mir.model.skill.packet;

/**
 * 使用技能 如果这个技能需要指向一个生物，那么就传生物id,并且x,y都为-1 否则的话，就将生物id设置成-1，x，y是真实的值
 * 
 * @author 37wan
 * 
 */
public class CM_UseSkill {

	private int skillId;
	private int x;
	private int y;
	private long targetId;
	/** 这个属性比较特别，当我们使用前端将非指向性技能转变成指向性技能的时候，就可以使用这个参数 */
	private long[] targetList;
	/** 方向 0-7 */
	private byte dirction;

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

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public long[] getTargetList() {
		return targetList;
	}

	public void setTargetList(long[] targetList) {
		this.targetList = targetList;
	}

	public byte getDirction() {
		return dirction;
	}

	public void setDirction(byte dirction) {
		this.dirction = dirction;
	}

}
