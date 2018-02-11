package com.mmorpg.mir.model.controllers.packet;

import java.util.List;

import com.mmorpg.mir.model.skill.effect.Effect;

public class SM_ABNORMAL_EFFECT {
	private long effectedId;
	private byte[] abnormals;
	private AbnormalEffect[] effects;

	public SM_ABNORMAL_EFFECT() {
	}

	public SM_ABNORMAL_EFFECT(long effectedId, byte[] abnormals, List<Effect> effects) {
		this.abnormals = abnormals;
		this.effectedId = effectedId;
		this.effects = new AbnormalEffect[effects.size()];
		int i = 0;
		for (Effect effect : effects) {
			this.effects[i] = effect.createAbnormalEffect();
			i++;
		}
	}
	
	public long getEffectedId() {
		return effectedId;
	}

	public void setEffectedId(long effectedId) {
		this.effectedId = effectedId;
	}

	public byte[] getAbnormals() {
		return abnormals;
	}

	public void setAbnormals(byte[] abnormals) {
		this.abnormals = abnormals;
	}

	public AbnormalEffect[] getEffects() {
		return effects;
	}

	public void setEffects(AbnormalEffect[] effects) {
		this.effects = effects;
	}

}
