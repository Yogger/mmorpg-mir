package com.mmorpg.mir.model.skill.model;

public class SpecialEffect {

	private long bloodSuckValue;
	private long reboundValue;

	public static SpecialEffect valueOf(long bloodSuckValue, long reboundValue) {
		SpecialEffect effect = new SpecialEffect();
		effect.bloodSuckValue = bloodSuckValue;
		effect.reboundValue = reboundValue;
		return effect;
	}

	public long getBloodSuckValue() {
		return bloodSuckValue;
	}

	public void setBloodSuckValue(long bloodSuckValue) {
		this.bloodSuckValue = bloodSuckValue;
	}

	public long getReboundValue() {
		return reboundValue;
	}

	public void setReboundValue(long reboundValue) {
		this.reboundValue = reboundValue;
	}

}
