package com.mmorpg.mir.model.skill.packet;

import com.mmorpg.mir.model.skill.model.SpecialEffect;

public class SM_PeriodicAction {
	private int skillId;
	private long hpDamage;
	private long objId;
	private long attackId;
	/** 反伤和吸血 */
	private SpecialEffect specialEffect;

	public static SM_PeriodicAction valueOf(long attckId, int skillId, long hpDamage, long objId,
			SpecialEffect specialEffect) {
		SM_PeriodicAction smp = new SM_PeriodicAction();
		smp.skillId = skillId;
		smp.hpDamage = hpDamage;
		smp.objId = objId;
		smp.specialEffect = specialEffect;
		smp.attackId = attckId;
		return smp;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public long getHpDamage() {
		return hpDamage;
	}

	public void setHpDamage(long hpDamage) {
		this.hpDamage = hpDamage;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public SpecialEffect getSpecialEffect() {
		return specialEffect;
	}

	public void setSpecialEffect(SpecialEffect specialEffect) {
		this.specialEffect = specialEffect;
	}

	public long getAttackId() {
		return attackId;
	}

	public void setAttackId(long attackId) {
		this.attackId = attackId;
	}

}
