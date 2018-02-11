package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.world.World;

public class FreeReliveEffect extends EffectTemplate {

	public static final String FREERELIVEEFFECT = "FREE_RELIVE";

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getObserveController().removeObserver(effect.getActionObserver());
	}

	@Override
	public void startEffect(final Effect effect) {
		ActionObserver actionObserver = new ActionObserver(ObserverType.SPAWN) {
			@Override
			public void spawn(int mapId, int instanceId) {
				if (World.getInstance().getMapResource(mapId).isKingCallSpecialMap()) {
					effect.getEffected().getEffectController().removeEffect(FREERELIVEEFFECT);
				}
			}
		};
		effect.setActionObserver(actionObserver);
		effect.getEffected().getObserveController().addObserver(actionObserver);
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
		effect.setReserved3(3);
	}

}