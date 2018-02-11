package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Date;

import com.mmorpg.mir.model.skill.effect.Effect;
import com.windforce.common.utility.DateUtils;

public class HeroRankEffect extends EffectTemplate {

	public static final String HERO_BUFF = "HERO_RANK";

	@Override
	public void calculate(Effect effect) {
		Long endTime = DateUtils.getNextDayFirstTime(new Date()).getTime();
		effect.setDuration(endTime - System.currentTimeMillis());
		effect.setEndTime(endTime);
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

}
