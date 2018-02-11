package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.event.MilitaryStrategyUpEvent;
import com.mmorpg.mir.model.quest.model.Quest;

public class MilitaryStrategyLevelCondition extends AbstractCoreCondition implements QuestCondition{

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
		
		Integer level = player.getMilitary().getStrategy().get(Integer.parseInt(code));
		if (level != null && level >= value) {
			return true;
		}
		
		throw new ManagedException(ManagedErrorCode.STRATEGY_LEVEL_NOT_ENOUGH);
	}

	@Override
    public Class<?>[] getEvent() {
		return new Class<?>[] {MilitaryStrategyUpEvent.class};
    }

}
