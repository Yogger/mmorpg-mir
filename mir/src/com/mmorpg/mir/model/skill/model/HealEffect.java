package com.mmorpg.mir.model.skill.model;

public class HealEffect {

	private long healDp;
	private long healHp;

	public static HealEffect valueOf(long healDp, long healHp) {
		HealEffect effect = new HealEffect();
		effect.healDp = healDp;
		effect.healHp = healHp;
		return effect;
	}

	public long getHealDp() {
		return healDp;
	}

	public void setHealDp(long healDp) {
		this.healDp = healDp;
	}

	public long getHealHp() {
		return healHp;
	}

	public void setHealHp(long healHp) {
		this.healHp = healHp;
	}
}
