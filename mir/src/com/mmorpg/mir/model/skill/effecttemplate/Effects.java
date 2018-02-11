package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.ArrayList;
import java.util.List;

public class Effects {

	protected List<EffectTemplate> effects = new ArrayList<EffectTemplate>(10);

	public List<EffectTemplate> getEffects() {
		return effects;
	}

	public void setEffects(List<EffectTemplate> effects) {
		this.effects = effects;
	}

	public void addEffect(EffectTemplate effectTemplate) {
		this.effects.add(effectTemplate);
	}

}
