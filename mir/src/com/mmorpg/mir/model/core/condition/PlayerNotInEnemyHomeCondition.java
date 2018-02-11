package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerNotInEnemyHomeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		// 王城地图ID
		String[] kingCityMapId = ConfigValueManager.getInstance().KING_CITY_MAPID.getValue();
		int playerCountryIndex = player.getCountryValue() - 1;
		for (int i = 0; i < kingCityMapId.length; i++) {
			if (i != playerCountryIndex && player.getMapId() == Integer.parseInt(kingCityMapId[i])) { 
				throw new ManagedException(ManagedErrorCode.PLAYER_AT_ENEMY_HOME);
			}
		}
		return true;
	}

}
