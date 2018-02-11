package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

public class NotLocalCountryCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Integer countryId = null;
		if (object instanceof Player) {
			countryId = ((Player) object).getCountryId().getValue();
		}
		if (object instanceof Integer) {
			countryId = (Integer) object;
		}
		if(countryId == null){
			this.errorObject(object);
		}
		if (countryId.intValue() != value) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.NOT_LOCAL_RESIDENT);
	}

}
