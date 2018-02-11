package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.purse.model.CurrencyType;

public class CurrencyCondition extends AbstractCoreCondition {

	private CurrencyType type;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getPurse().isEnough(getType(), value)) {
			// 这里根据类型抛出异常
			throw new ManagedException(getType().getErrorCode());
		}
		return true;
	}

	// @Override
	// protected void init() {
	// setType(CurrencyType.typeOf(code));
	// }

	@Override
	protected void init() {
		setType(CurrencyType.valueOf(Integer.valueOf(code)));
	}

	public CurrencyType getType() {
		return type;
	}

	public void setType(CurrencyType type) {
		this.type = type;
	}

}
