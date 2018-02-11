package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 官职
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryNotForbidChatCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}

		if (!player.getCountry().isForbidchat(player.getObjectId())) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.COUNTRY_FORBIDCHAT);
	}
}
