package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class ModuleOpenCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(player);
		}

		Boolean open = player.getModuleOpen().getOpeneds().get(code);
		if (open == null || open.booleanValue() == false) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		return true;
	}

}
