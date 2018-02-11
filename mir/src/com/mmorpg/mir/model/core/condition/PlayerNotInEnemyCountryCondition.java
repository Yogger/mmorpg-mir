package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapCountry;

public class PlayerNotInEnemyCountryCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		int mapCountry = World.getInstance().getMapResource(player.getMapId()).getCountry();
		if (mapCountry != MapCountry.NEUTRAL.getValue() && mapCountry != player.getCountryValue()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_ENEMY_COUNTRY);
		}
		
		return true;
	}

}
