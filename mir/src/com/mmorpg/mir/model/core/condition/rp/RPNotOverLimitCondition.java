package com.mmorpg.mir.model.core.condition.rp;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

public class RPNotOverLimitCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getRp().getRp() >= value) { 
			throw new ManagedException(ManagedErrorCode.ITEM_ADD_RP_LIMIT);
		}
		return true;
	}

}
