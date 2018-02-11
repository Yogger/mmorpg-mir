package com.mmorpg.mir.model.item.model;

import java.util.BitSet;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.storage.PlayerItemStorage;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PacketInit extends ModuleHandle {

	@Autowired
	private ItemManager itemManager;

	@Override
	public ModuleKey getModule() {
		return ModuleKey.PACKAGE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getPackJson() == null) {
			PlayerItemStorage storage = PlayerItemStorage.valueOf(itemManager.getInitSize((byte) 0),
					itemManager.getMaxSize((byte) 0));
			storage.calculateWaitTime(itemManager.getReaminTime(storage, (byte) 0));
			ent.setPackJson(JsonUtils.object2String(storage));
		}

		Player player = ent.getPlayer();

		if (player.getPack() == null) {
			PlayerItemStorage pack = JsonUtils.string2Object(ent.getPackJson(), PlayerItemStorage.class);
			if (pack.getTotalLimit() == null) {
				pack.setTotalLimit(new HashMap<String, Integer>());
			}
			int length = pack.getItems().length;
			pack.setMarks(new BitSet(length));
			player.setPack(pack);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getPack() != null) {
			ent.setPackJson(JsonUtils.object2String(player.getPack()));
		}
	}

	public static void main(String[] args) {
		PlayerItemStorage storage = PlayerItemStorage.valueOf(6, 33);
		storage.calculateWaitTime(123123);
		System.out.println(JsonUtils.object2String(storage));
	}
}
