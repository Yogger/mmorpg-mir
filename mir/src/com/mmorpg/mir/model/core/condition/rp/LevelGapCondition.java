package com.mmorpg.mir.model.core.condition.rp;

import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

public class LevelGapCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		Player other = null;
		
		
		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);
			other = (Player) ((Map<String, Object>) object).get(TriggerContextKey.OTHER_PLAYER);
		}
		
		if (player == null || other == null) {
			this.errorObject(object);
		}
		
		if (player.getLevel() - other.getLevel() <= value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.LEVEL_GAP_OVERFLOW);
	}

}
