package com.mmorpg.mir.model.core.condition.openactive;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;

public class OpenActiveCompeteCondition extends AbstractCoreCondition {

	private int rankType;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		return player.getOpenActive().getCompeteRankActivity(rankType).getCompeteValue() >= value;	
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		rankType = CompeteRankValue.findRank(Integer.valueOf(code)).getRankTypeValue();
	}

}
