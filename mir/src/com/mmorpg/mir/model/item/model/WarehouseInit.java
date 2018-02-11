package com.mmorpg.mir.model.item.model;

import java.util.BitSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class WarehouseInit extends ModuleHandle {

	@Autowired
	private ItemManager itemManager;

	@Override
	public ModuleKey getModule() {
		return ModuleKey.WAREHOUSE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getWareHouseJson() == null) {
			ItemStorage storage = ItemStorage.valueOf(itemManager.getInitSize((byte) 1),
					itemManager.getMaxSize((byte) 1));
			storage.calculateWaitTime(itemManager.getReaminTime(storage, (byte) 1));
			ent.setWareHouseJson(JsonUtils.object2String(storage));
		}

		Player player = ent.getPlayer();

		if (player.getWareHouse() == null) {
			ItemStorage pack = JsonUtils.string2Object(ent.getWareHouseJson(), ItemStorage.class);
			int length = pack.getItems().length;
			pack.setMarks(new BitSet(length));
			player.setWareHouse(pack);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getWareHouse() != null) {
			ent.setWareHouseJson(JsonUtils.object2String(player.getWareHouse()));
		}
	}
}
