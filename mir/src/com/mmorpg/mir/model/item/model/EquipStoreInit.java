package com.mmorpg.mir.model.item.model;

import java.util.BitSet;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class EquipStoreInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.EQUIPMENT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getEquipStoreJson() == null) {
			ent.setEquipStoreJson(JsonUtils.object2String(EquipmentStorage.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getEquipmentStorage() == null) {
			EquipmentStorage eStore = JsonUtils.string2Object(ent.getEquipStoreJson(), EquipmentStorage.class);
			int length = eStore.getEquipments().length;
			eStore.setMark(new BitSet(length));
			player.setEquipmentStorage(eStore);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getEquipmentStorage() != null) {
			ent.setEquipStoreJson(JsonUtils.object2String(player.getEquipmentStorage()));
		}
	}
}
