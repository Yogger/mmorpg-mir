package com.mmorpg.mir.model.core.condition;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

public class KillingItemCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);
		}
		if (player == null) {
			this.errorObject(object);
		}
		
		Integer count = player.getCombatSpiritStorage().getItemDailyLimit().get(code);
		if (count == null) {
			count = 0;
		}
		
		return count <= value;
	}

}
