package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Integer countryId = null;
		if (object instanceof Player) {
			countryId = ((Player) object).getCountryId().getValue();
		}
		if (object instanceof Integer) {
			countryId = (Integer) object;
		}
		if (countryId== null) {
			this.errorObject(object);
		}
		if (countryId.intValue() == value) {
			return true;
		}

		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
