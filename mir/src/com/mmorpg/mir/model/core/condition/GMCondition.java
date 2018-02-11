package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.SpringComponentStation;
import com.mmorpg.mir.model.gameobjects.Player;

public class GMCondition extends AbstractCoreCondition{

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		boolean gm = SpringComponentStation.getOperatorManager().getGmList().isGM(player.getObjectId());

		if ("1".equals(this.code) && gm) {
			return true;
		}
		if ("0".equals(this.code) && !gm) {
			return true;
		}
		return false;
	}

}
