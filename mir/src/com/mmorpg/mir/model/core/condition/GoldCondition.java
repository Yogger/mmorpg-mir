package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.purse.model.CurrencyType;

public class GoldCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getPurse().getValue(CurrencyType.GOLD) < value) {
			// 这里根据类型抛出异常
			throw new ManagedException(CurrencyType.GOLD.getErrorCode());
		}
		return true;
	}

}
