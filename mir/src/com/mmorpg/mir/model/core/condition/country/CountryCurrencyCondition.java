package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryCurrencyCondition extends AbstractCoreCondition {

	private CoppersType type;

	@Override
	public boolean verify(Object object) {
		Country country = null;
		if (object instanceof Country) {
			country = (Country) object;
		}
		if (object instanceof Player) {
			country = ((Player) object).getCountry();
		}

		if (country == null) {
			errorObject(object);
		}
		if (!country.getCoppers().isEnough(getType(), Long.valueOf(value))) {
			// 这里根据类型抛出异常
			throw new ManagedException(getType().getErrorCode());
		}
		return true;
	}

	@Override
	protected void init() {
		setType(CoppersType.typeOf(code));
	}

	// @Override
	// protected void init() {
	// setType(CoppersType.valueOf(Integer.valueOf(code)));
	// }

	public CoppersType getType() {
		return type;
	}

	public void setType(CoppersType type) {
		this.type = type;
	}

}
