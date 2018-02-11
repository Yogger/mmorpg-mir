package com.mmorpg.mir.model.rescue.model.typehandle;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rescue.model.RescueItem;
import com.mmorpg.mir.model.rescue.model.RescueType;
import com.mmorpg.mir.model.rescue.resource.RescueResource;

public interface RescueTypeHandle {

	public RescueType getRescueType();

	/**
	 * 生成一个新的
	 * 
	 * @param resource
	 * @param player
	 * @return
	 */
	public RescueItem create(RescueResource resource, Player player);

}
