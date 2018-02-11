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
public class HorseEquipStoreInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.HORSE_EQUIPSTORE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getHorseEquipStoreJson() == null) {
			ent.setHorseEquipStoreJson(JsonUtils.object2String(EquipmentStorage.valueOfHorse()));
		}

		Player player = ent.getPlayer();

		if (player.getHorseEquipmentStorage() == null) {
			EquipmentStorage horseEquipStore = JsonUtils.string2Object(ent.getHorseEquipStoreJson(),
					EquipmentStorage.class);
			int length = horseEquipStore.getEquipments().length;
			horseEquipStore.setMark(new BitSet(length));
			player.setHorseEquipmentStorage(horseEquipStore);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getHorseEquipmentStorage() != null) {
			ent.setHorseEquipStoreJson(JsonUtils.object2String(player.getHorseEquipmentStorage()));
		}

	}
}
