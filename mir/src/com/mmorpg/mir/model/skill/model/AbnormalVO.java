package com.mmorpg.mir.model.skill.model;

import java.util.List;

import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.packet.AbnormalEffect;
import com.mmorpg.mir.model.skill.effect.Effect;

public class AbnormalVO {

	private byte[] abnormals;
	private AbnormalEffect[] effects;

	public AbnormalVO() {
	}

	public AbnormalVO(EffectController effectController) {
		List<Effect> effects = effectController.getAbnormalEffects();
		this.abnormals = effectController.bitSet2ByteArray();
		this.effects = new AbnormalEffect[effects.size()];
		int i = 0;
		for (Effect effect : effects) {
			this.effects[i] = effect.createAbnormalEffect();
			i++;
		}
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
