package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.event.BecomeReseveKingEvent;
import com.mmorpg.mir.model.country.event.ReserveKingAbdicateEvent;
import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;

/**
 * 玩家是储君
 * 
 * @author 37.com
 * 
 */
public class PlayerIsReserveKing extends AbstractCoreCondition implements NicknameCondition {

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] { ReserveKingAbdicateEvent.class, BecomeReseveKingEvent.class };
	}

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getCountryValue() != this.value) {
			return false;
		}

		ReserveKing reserveKing = player.getCountry().getReserveKing();

		return reserveKing.isReserveKing(player.getObjectId());
	}
}
