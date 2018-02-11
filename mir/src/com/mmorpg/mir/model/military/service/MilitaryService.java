package com.mmorpg.mir.model.military.service;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.packet.CM_Break_MilitaryStratege;
import com.mmorpg.mir.model.military.packet.CM_strategy_upgrade;

public interface MilitaryService {
	/**
	 * 杀人添加荣誉
	 * 
	 * @param killer
	 * @param killed
	 */
	void killAddHonor(Player killer, Player killed);

	/**
	 * 军衔升级
	 * 
	 * @param player
	 */
	void upgradeMilitaryRank(Player player);

	/**
	 * 军衔升星
	 * 
	 * @param player
	 */
	void upgradeMilitaryStar(Player player);

	/**
	 * 军衔兵法升级
	 * 
	 * @param player
	 * @param req
	 */
	void upgradeMilitaryStrategy(Player player, CM_strategy_upgrade req);
	
	/**
	 * 军衔兵法突破
	 * @param player
	 * @param req
	 */
	void breakMilitaryStrategy(Player player, CM_Break_MilitaryStratege req);
}
