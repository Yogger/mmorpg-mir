package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.country.model.TraitorPlayerFix;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effect.Effect;

public class TraitorEffect extends EffectTemplate {

	public static final String TRAITOR = "TRAITOR";

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
	}

	@Override
	public void startEffect(final Effect effect) {
	}

	@Override
	public void calculate(Effect effect) {
		Player player = (Player) effect.getEffected();
		TraitorPlayerFix tp = player.getCountry().getTraitorMapFixs().get(player.getObjectId());
		effect.setDuration(tp.getRemainTime());
		effect.setEndTime(System.currentTimeMillis() + tp.getRemainTime());
		effect.addSucessEffect(this);
	}
	
}