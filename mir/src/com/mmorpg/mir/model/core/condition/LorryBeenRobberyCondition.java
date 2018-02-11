package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class LorryBeenRobberyCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}			
		
		if (player.getExpress().hadLorry()) {
			return player.getExpress().getCurrentLorry().isRob();
		}
		return false;
	}

}
