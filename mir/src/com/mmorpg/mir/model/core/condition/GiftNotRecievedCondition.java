package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class GiftNotRecievedCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		if (player.getWelfare().oneOffGiftIsReward(value)) {
			throw new ManagedException(ManagedErrorCode.GIFT_REWARDED);
		}
		
		return true;
	}

}
