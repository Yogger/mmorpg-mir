package com.mmorpg.mir.model.soul.core;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Uplevel;

public interface SoulService {
	void initSoulStats(Player player);

	/**
	 * 
	 * @param player
	 * @param autoBuy
	 * @return
	 */
	SM_Soul_Uplevel uplevel(Player player, boolean autoBuy);

	/**
	 * 刷新
	 * 
	 * @param player
	 * @param upLevel
	 */
	void flushSoul(Player player, boolean upLevel, boolean clear);

}
