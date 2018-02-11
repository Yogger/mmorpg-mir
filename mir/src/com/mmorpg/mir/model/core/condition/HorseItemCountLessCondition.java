package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.resource.HorseGrowItemResource;

public class HorseItemCountLessCondition extends AbstractCoreCondition {

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
		if (player.getHorse().getGrowItemCount().containsKey(this.code)) {
			count = player.getHorse().getGrowItemCount().get(this.code);
		}
		HorseGrowItemResource resource = HorseManager.getInstance().horseGrowItemStorage.get(this.code, true);
		int maxCount = resource.getItemCountLimit().get(player.getHorse().getGrade() + "");
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.HORSE_ITEM_COUNT_LIMIT);
	}

}
