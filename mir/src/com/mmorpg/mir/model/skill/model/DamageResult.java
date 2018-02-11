package com.mmorpg.mir.model.skill.model;

public class DamageResult {

	private long damage;

	public static DamageResult valueOf() {
		DamageResult dr = new DamageResult();
		return dr;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}
}
