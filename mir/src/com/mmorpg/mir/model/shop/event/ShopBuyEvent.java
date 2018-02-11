package com.mmorpg.mir.model.shop.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class ShopBuyEvent implements IEvent {

	private long playerId;
	
	private String shopId;
	
	public static ShopBuyEvent valueOf(Player player, String id) {
		ShopBuyEvent e = new ShopBuyEvent();
		e.playerId = player.getObjectId();
		e.shopId = id;
		return e;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
