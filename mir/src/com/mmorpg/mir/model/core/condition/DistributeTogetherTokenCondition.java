package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

/** 虎符人数限制 */
public class DistributeTogetherTokenCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getCountry().getDiplomacy().getCallTogetherTokens().size() < value) {
			return true;
		}
       throw  new ManagedException(ManagedErrorCode.COUNTRY_CALLTOGETHER_TOKEN_OUT);
	}

}
