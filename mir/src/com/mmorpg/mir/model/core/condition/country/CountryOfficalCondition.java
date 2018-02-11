package com.mmorpg.mir.model.core.condition.country;

import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * 官职
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryOfficalCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		CountryOfficial officialType = null;
		if (object instanceof Player) {
			Player player = (Player) object;
			Official offical = player.getCountry().getPlayerOffical(player.getObjectId());
			if (offical == null) {
				officialType = CountryOfficial.CITIZEN;
			} else {
				officialType = offical.getOfficial();
			}
		}
		if (object instanceof Official) {
			Official offical = (Official) object;
			officialType = offical.getOfficial();
		}
		
		if (object instanceof Map) {
			Player player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.OTHER_PLAYER);
			Official offical = player.getCountry().getPlayerOffical(player.getObjectId());
			if (offical == null) {
				officialType = CountryOfficial.CITIZEN;
			} else {
				officialType = offical.getOfficial();
			}
		}
		if (officialType == null) {
			this.errorObject(object);
		}

		if (officialType == CountryOfficial.typeOf(code)) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_OFFICAL);
	}
}
