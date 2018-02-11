package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 是警卫队
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryLessGuardCountCondition extends AbstractCoreCondition {

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
		if (country.getCourt().getKingGuards().size() < value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COUNTRY_GURAD_MAX);
	}
}
