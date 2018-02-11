package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.beauty.model.BeautyGirl;
import com.mmorpg.mir.model.beauty.model.BeautyGirlPool;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class BeautyGirlLevelGreaterCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {

		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		BeautyGirlPool pool = player.getBeautyGirlPool();

		if (!pool.isActive(this.code)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_TARGET_NOT_ACTIVE);
		}

		BeautyGirl girl = pool.getBeautyGirls().get(this.code);
		if (girl.getLevel() > this.value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.BEAUTY_GIRL_LEVEL_NOT_ENOUGH);
	}
}
