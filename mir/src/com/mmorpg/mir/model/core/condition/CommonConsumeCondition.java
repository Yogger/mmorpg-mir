package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class CommonConsumeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		return player.getCommonActivityPool().getConsumeActives().get(this.code).getConsumeGold() >= value;
	}
}
