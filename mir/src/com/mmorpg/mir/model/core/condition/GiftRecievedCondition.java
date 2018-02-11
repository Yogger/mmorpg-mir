package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.welfare.event.GiftrecievedEvent;

public class GiftRecievedCondition extends AbstractCoreCondition implements QuestCondition{

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
		
		return player.getWelfare().oneOffGiftIsReward(value);
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] {GiftrecievedEvent.class};
	}

}
