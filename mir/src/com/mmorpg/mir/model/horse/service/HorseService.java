package com.mmorpg.mir.model.horse.service;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;

public interface HorseService {

	/**
	 * 骑乘
	 * 
	 * @param player
	 */
	void ride(Player player);

	/**
	 * 下马
	 * 
	 * @param player
	 */
	void unRide(Player player);

	/**
	 * 升级坐骑
	 * 
	 * @param player
	 * @param autoBuy
	 * @return
	 */
	SM_HorseUpdate upgradeHorse(Player player, boolean autoBuy);

	/**
	 * 刷新坐骑
	 * 
	 * @param player
	 * @param isUpLevel
	 * @param sendPacket
	 */
	void flushHorse(Player player, boolean isUpgrade, boolean clear, boolean sendPacket);

	/**
	 * 添加人物属性
	 * 
	 * @param player
	 */
	void addStats(Player player);

	/**
	 * 坐骑幻化
	 * 
	 * @param player
	 * @param foreverActive
	 *            是否永久激活
	 */
	void activeIllution(Player player, boolean foreverActive, int sign);

	/**
	 * 幻化取消
	 * 
	 * @param player
	 * @param type
	 */
	void cancelActiveIllution(Player player, int sign);

	/**
	 * 幻化生效
	 * 
	 * @param player
	 * @param type
	 */
	void useIllution(Player player, int sign);

	public void addGrade(Player player);

	public void useBlessItem(Player player, int blessValue);
}
