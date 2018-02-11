package com.mmorpg.mir.model.transport.service;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.transport.packet.SM_PlayerTransport;
import com.mmorpg.mir.model.transport.packet.SM_Transport;

public interface TransportService {
	/**
	 * 传送
	 * 
	 * @param id
	 * @param player
	 * @return
	 */
	SM_Transport transport(int id, Player player);

	/**
	 * 飞鞋传送
	 * 
	 * @param id
	 * @param player
	 * @return
	 */
	SM_PlayerTransport playerTransport(int id, Player player);

	/**
	 * 聊天传送
	 * 
	 * @param id
	 * @param player
	 */
	void playerChatTransport(int id, Player player);
}
