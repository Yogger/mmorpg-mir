package com.mmorpg.mir.model.core.condition.rp;

import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effecttemplate.GrayEffect;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * code 记录时间, value 记录次数
 */
public class PlayerInWeakCondition extends AbstractCoreCondition{

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.OTHER_PLAYER);
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		if (player.getEffectController().contains(GrayEffect.GRAY)) {
			return true;
		}
		
		throw new ManagedException(ManagedErrorCode.PLAYER_IN_WEAK);
	}

}
