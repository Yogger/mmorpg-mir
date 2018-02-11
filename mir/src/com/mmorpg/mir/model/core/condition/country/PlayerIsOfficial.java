package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerIsOfficial extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getCountry().getCourt().isGurad(player.getObjectId())) {
			return true;
		}
		Official of = player.getCountry().getPlayerOffical(player.getObjectId());
		if (of == null || of.getOfficial() == CountryOfficial.CITIZEN) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_GET_OFFICIAL_ERROR);
		}
		return true;
	}

}
