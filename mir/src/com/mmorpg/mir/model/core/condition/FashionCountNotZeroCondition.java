package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.gameobjects.Player;

public class FashionCountNotZeroCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {

		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}
		FashionPool pool = player.getFashionPool();
		int size = pool.getOwnFashions().size();
		if (size == 0) {
			throw new ManagedException(ManagedErrorCode.FASHION_COUNT_EQUAL_ZERO);
		}
		return true;
	}
}
