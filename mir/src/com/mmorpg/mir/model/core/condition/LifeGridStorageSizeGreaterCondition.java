package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.lifegrid.model.LifeGridStorage;
import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;

public class LifeGridStorageSizeGreaterCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {

		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}
		LifeStorageType type = LifeStorageType.typeOf(Integer.parseInt(this.code));
		LifeGridStorage storage = player.getLifeGridPool().getStorageByType(type);
		if (storage.getEmptySize() <= this.value) {
			int code = ManagedErrorCode.LIFE_GRID_PACK_NOT_ENOUGH_SIZE;
			if (type == LifeStorageType.HOUSE) {
				code = ManagedErrorCode.LIFE_GRID_HOUSE_NOT_ENOUGH_SIZE;
			}
			throw new ManagedException(code);
		}

		return true;
	}
}
