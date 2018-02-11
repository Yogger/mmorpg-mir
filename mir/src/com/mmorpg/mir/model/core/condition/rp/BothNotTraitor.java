package com.mmorpg.mir.model.core.condition.rp;

import java.util.Map;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

public class BothNotTraitor extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		Player other = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (object instanceof Map) {
			other = (Player) ((Map<String, Object>) object).get(TriggerContextKey.OTHER_PLAYER);
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);
		}
		if (player == null) {
			this.errorObject(object);
		}

		if (!player.isTraitor() && !(other != null && other.isTraitor()))
			return true;

		return false;

	}

}
