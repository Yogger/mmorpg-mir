package com.mmorpg.mir.model.core.condition.openactive;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

public class OpenActiveConsumeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		if (player.getOpenActive().getConsumeActive().getConsumeGold() < value) {
			throw new ManagedException(ManagedErrorCode.CONSUME_ACTIVE_NOT_ENOUGH); 
		}
		return true;
	}

}
