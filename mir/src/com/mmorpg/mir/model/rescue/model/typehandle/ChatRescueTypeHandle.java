package com.mmorpg.mir.model.rescue.model.typehandle;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rescue.model.RescueItem;
import com.mmorpg.mir.model.rescue.model.RescueType;
import com.mmorpg.mir.model.rescue.resource.RescueResource;

public class ChatRescueTypeHandle implements RescueTypeHandle {

	@Override
	public RescueType getRescueType() {
		return RescueType.CHAT;
	}

	@Override
	public RescueItem create(RescueResource resource, Player player) {
		return RescueItem.valueOf(resource);
	}

}
