package com.mmorpg.mir.model.skill.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_Attack {
	private long effector;
	private long target;
	private int hpDamage;
	private byte damageType;

	public long getEffector() {
		return effector;
	}

	public void setEffector(long effector) {
		this.effector = effector;
	}

	public long getTarget() {
		return target;
	}

	public void setTarget(long target) {
		this.target = target;
	}

	public int getHpDamage() {
		return hpDamage;
	}

	public void setHpDamage(int hpDamage) {
		this.hpDamage = hpDamage;
	}

	public byte getDamageType() {
		return damageType;
	}

	public void setDamageType(byte damageType) {
		this.damageType = damageType;
	}

	public static SM_Attack valueOf(Creature effector, Creature target, byte damageType, int hpDamage) {
		SM_Attack result = new SM_Attack();
		result.effector = effector.getObjectId();
		result.target = target.getObjectId();
		result.damageType = damageType;
		result.hpDamage = hpDamage;
		return result;
	}
}
