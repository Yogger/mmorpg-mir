package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.resource.PlayerLevelResource;

public class SuicidePlayerLevelCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == object) {
			this.errorObject(object);
		}

		int level = player.getLevel();

		if (level < this.value) {
			PlayerLevelResource resource = PlayerManager.getInstance().getPlayerLevelResource(this.value);
			
			int errorCode = resource.getTurnNum() != 0 ? ManagedErrorCode.SUICIDE_LEVEL_N0T_ENOUGH
					: ManagedErrorCode.LEVEL_NOT_ENOUGH;
			throw new ManagedException(errorCode);
		}

		return true;
	}
}
