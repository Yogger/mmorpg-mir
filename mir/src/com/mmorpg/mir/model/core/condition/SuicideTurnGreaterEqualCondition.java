package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class SuicideTurnGreaterEqualCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		int turn = player.getSuicide().getTurn();

		if (turn < this.value) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_TURN_NOT_ENOUGH);
		}

		return true;
	}

}
