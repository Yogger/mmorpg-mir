package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.resource.SoulGrowItemResource;

public class SoulItemCountLessCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		int count = 0;
		if (player.getSoul().getGrowItemCount().containsKey(this.code)) {
			count = player.getSoul().getGrowItemCount().get(this.code);
		}
		SoulGrowItemResource resource = SoulManager.getInstance().soulGrowItemStorage.get(this.code, true);

		int maxCount = resource.getItemCountLimit().get(player.getSoul().getLevel() + "");
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.SOUL_ITEM_COUNT_LIMIT);
	}

}
