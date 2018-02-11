package com.mmorpg.mir.model.rescue.model.typehandle;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rescue.model.RescueItem;
import com.mmorpg.mir.model.rescue.model.RescueType;
import com.mmorpg.mir.model.rescue.resource.RescueResource;

public class MonsterRescueTypeHandle implements RescueTypeHandle {

	@Override
	public RescueType getRescueType() {
		return RescueType.MONSTER;
	}

	@Override
	public RescueItem create(RescueResource resource, Player player) {
		return RescueItem.valueOf(resource);
	}

}
