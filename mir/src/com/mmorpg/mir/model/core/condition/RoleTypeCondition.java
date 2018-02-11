package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class RoleTypeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		Quest quest = null; 
		if (object instanceof Player) {
			player = (Player) object;
			if (player.getRole() == value) {
				return true;
			}
		}
		if (object instanceof Quest) {
			quest = (Quest) object;
			if (quest.getOwner().getRole() == value) {
				return true;
			}
		}
		
		if (quest == null && player == null) { 
			this.errorObject(object);
		}
		
		return false;
	}

}
