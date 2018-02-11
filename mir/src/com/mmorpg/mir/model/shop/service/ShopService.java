package com.mmorpg.mir.model.shop.service;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.shop.model.BuyResult;

public interface ShopService {
	/**
	 * 购买
	 * 
	 * @param player
	 * @param id
	 * @param count
	 */
	void buy(Player player, String id, int count);

	public BuyResult autoBuy(Player player, String id, int count);
}
