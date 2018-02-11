package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.kingofwar.event.BecomeKingOfKingEvent;
import com.mmorpg.mir.model.kingofwar.event.KingOfKingAbdicateEvent;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.quest.model.Quest;

public class KingOfKingCondition extends AbstractCoreCondition implements NicknameCondition{

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if(player == null){
			this.errorObject(object);
		}
		boolean isKingOfKing = player.isKingOfking();
		if (isKingOfKing && value == 1) { // 是皇帝
			return true;
		}
		if ((!isKingOfKing) && value == 0) { //不是皇帝
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] {BecomeKingOfKingEvent.class, KingOfKingAbdicateEvent.class};
	}

}
