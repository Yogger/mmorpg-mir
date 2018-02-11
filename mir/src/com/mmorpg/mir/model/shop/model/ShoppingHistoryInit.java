package com.mmorpg.mir.model.shop.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class ShoppingHistoryInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.SHOP;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getShoppingHistoryJson() == null) {
			ent.setShoppingHistoryJson(JsonUtils.object2String(ShoppingHistory.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getShoppingHistory() == null) {
			ShoppingHistory purse = JsonUtils.string2Object(ent.getShoppingHistoryJson(), ShoppingHistory.class);
			player.setShoppingHistory(purse);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getShoppingHistory() != null) {
			ent.setShoppingHistoryJson(JsonUtils.object2String(player.getShoppingHistory()));
		}
	}
}
