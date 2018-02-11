package com.mmorpg.mir.model.skill.model;

import com.mmorpg.mir.model.skill.effect.Effect;

public class EffectVO {

	private long targetId;
	private long hpDamage;
	private byte damageType;
	/** 反伤和吸血 */
	private SpecialEffect specialEffect;
	/** 回血回怒气 */
	private HealEffect healEffect;

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public long getHpDamage() {
		return hpDamage;
	}

	public void setHpDamage(long hpDamage) {
		this.hpDamage = hpDamage;
	}

	public byte getDamageType() {
		return damageType;
	}

	public void setDamageType(byte damageType) {
		this.damageType = damageType;
	}

	public static EffectVO valueOf(Effect effect) {
		EffectVO vo = new EffectVO();
		vo.targetId = effect.getEffected().getObjectId();
		vo.hpDamage = effect.getReserved1();
		vo.damageType = effect.getDamageType();
		vo.specialEffect = effect.getSpecialEffect();
		vo.healEffect = effect.getHealEffect();
		return vo;
	}

	public SpecialEffect getSpecialEffect() {
		return specialEffect;
	}

	public void setSpecialEffect(SpecialEffect specialEffect) {
		this.specialEffect = specialEffect;
	}

	public HealEffect getHealEffect() {
		return healEffect;
	}

	public void setHealEffect(HealEffect healEffect) {
		this.healEffect = healEffect;
	}

}
