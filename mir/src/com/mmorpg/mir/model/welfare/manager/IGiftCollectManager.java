package com.mmorpg.mir.model.welfare.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.packet.SM_GiftCollect_Open;

public interface IGiftCollectManager {
//	public void tryFinish(Player player, String resourceId);

	public SM_GiftCollect_Open getGiftCollect_Open(Player player);
}
