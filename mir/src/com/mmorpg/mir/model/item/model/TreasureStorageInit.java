package com.mmorpg.mir.model.item.model;

import java.util.BitSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.storage.TreasureItemStorage;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class TreasureStorageInit extends ModuleHandle {

	@Autowired
	private ItemManager itemManager;

	@Override
	public ModuleKey getModule() {
		return ModuleKey.TREASURE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getTreasureStorageJson() == null) {
			TreasureItemStorage storage = TreasureItemStorage.valueOf(
					itemManager.TREASURE_STORAGE_INIT_SIZE.getValue(),
					itemManager.TREASURE_STORAGE_INIT_SIZE.getValue());
			ent.setTreasureStorageJson(JsonUtils.object2String(storage));
		}

		Player player = ent.getPlayer();

		if (player.getTreasureWareHouse() == null) {
			TreasureItemStorage pack = JsonUtils.string2Object(ent.getTreasureStorageJson(), TreasureItemStorage.class);
			int length = pack.getItems().length;
			pack.setMarks(new BitSet(length));
			player.setTreasureWareHouse(pack);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getTreasureWareHouse() != null) {
			ent.setTreasureStorageJson(JsonUtils.object2String(player.getTreasureWareHouse()));
		}
	}

}
