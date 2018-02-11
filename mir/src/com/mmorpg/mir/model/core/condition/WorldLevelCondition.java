package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;

public class WorldLevelCondition extends AbstractCoreCondition {
	
	private Operator op;
	
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
		
		int level = player.getLevel();
		int worldLevel = WorldRankManager.getInstance().getWorldLevel();
		
		if (op == Operator.GREATER_EQUAL) {
			return level >= worldLevel;
		} else if (op == Operator.GREATER) {
			return level > worldLevel;
		} else if (op == Operator.EQUAL) {
			return level == worldLevel;
		} else if (op == Operator.LESS_EQUAL) {
			return level <= worldLevel;
		} else if (op == Operator.LESS) {
			return level < worldLevel;
		}
		
		return false;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}


}
