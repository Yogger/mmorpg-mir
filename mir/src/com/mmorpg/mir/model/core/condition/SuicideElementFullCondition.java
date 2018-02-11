package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class SuicideElementFullCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		if (player.getSuicide().getTurn() != this.value) {
			return false;
		}

		if (!player.getSuicide().isAllElementFull()) {
			return false;
		}

		return true;
	}
}
