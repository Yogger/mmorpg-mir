package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class HorseNotLearnSKillCondition extends AbstractCoreCondition{

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
		
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		
		if(player == null){
			this.errorObject(object);
		}
		if (!player.getHorse().getLearnedSkills().containsValue(value)) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.HORSE_LEARNED_SKILL);
	}

}
