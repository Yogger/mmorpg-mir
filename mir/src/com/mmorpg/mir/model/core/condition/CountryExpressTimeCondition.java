package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class CountryExpressTimeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
	Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}

		if (System.currentTimeMillis() - player.getCountry().getCountryQuest().getExpressEndTime() < 0) {
			return true;
		}
		
		return false;
	}

}
