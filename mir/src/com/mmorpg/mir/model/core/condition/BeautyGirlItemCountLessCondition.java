package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlItemResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class BeautyGirlItemCountLessCondition extends AbstractCoreCondition {

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
		if (player.getBeautyGirlPool().getItemCounts().containsKey(this.code)) {
			count = player.getBeautyGirlPool().getItemCounts().get(this.code);
		}
		BeautyGirlItemResource resource = BeautyGirlConfig.getInstance().beautyGirlItemStorage.get(this.code, true);
		int maxCount = resource.getMaxCount(player.getBeautyGirlPool().getSumLevel());
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.BEAUTY_GIRL_ITEM_COUNT_LIMIT);
	}

}
