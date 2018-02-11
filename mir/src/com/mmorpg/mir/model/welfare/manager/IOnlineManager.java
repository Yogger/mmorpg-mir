package com.mmorpg.mir.model.welfare.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.resource.OnlineResource;

public interface IOnlineManager {
	OnlineResource getOnlineResource(int index);

	void startOnlineCount(Player player);
}
