package com.mmorpg.mir.model.item.event;

import java.util.List;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.windforce.common.event.event.IEvent;

public class CollectItemsEvent implements IEvent {

	private long playerId;

	private List<AbstractItem> items;

	@Override
	public long getOwner() {
		return playerId;
	}

	public static CollectItemsEvent valueOf(Player player, List<AbstractItem> items) {
		CollectItemsEvent event = new CollectItemsEvent();
		event.items = items;
		event.playerId = player.getObjectId();
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public List<AbstractItem> getItems() {
		return items;
	}

	public void setItems(List<AbstractItem> items) {
		this.items = items;
	}

}
