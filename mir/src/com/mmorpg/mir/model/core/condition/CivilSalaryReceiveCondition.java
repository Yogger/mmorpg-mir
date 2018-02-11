package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.country.event.ReceiveCivilSalaryEvent;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 当天国民福利是否已领取
 * 
 * @author 37.com
 * 
 */
public class CivilSalaryReceiveCondition extends AbstractCoreCondition implements GiftCollectCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		boolean receive = player.getCountry().getCourt().getCivilReceived().contains(player.getObjectId());
		boolean flag = Boolean.valueOf(this.code);

		return receive == flag;

	}

	@Override
	public Class<?>[] getGiftCollectEvent() {
		return new Class<?>[] { ReceiveCivilSalaryEvent.class };
	}
}
