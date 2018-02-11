package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class EveryDayRechargeRewardCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}

		if (!player.getOpenActive().getEveryDayRecharge().getRewarded().contains(this.code)) {
			return true;
		} else {
			return false;
		}

	}
}