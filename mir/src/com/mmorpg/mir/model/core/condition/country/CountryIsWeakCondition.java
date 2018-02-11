package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryIsWeakCondition extends AbstractCoreCondition{

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}

		return player.getCountry().isWeakCountry();
	}
}
