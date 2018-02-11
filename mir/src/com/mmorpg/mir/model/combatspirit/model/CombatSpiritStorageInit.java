package com.mmorpg.mir.model.combatspirit.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CombatSpiritStorageInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COMBAT_SPIRIT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getCombatSpiritJson() == null) {
			ent.setCombatSpiritJson(JsonUtils.object2String(CombatSpiritStorage.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getCombatSpiritStorage() == null) {
			CombatSpiritStorage cStore = JsonUtils.string2Object(ent.getCombatSpiritJson(), CombatSpiritStorage.class);
			// cStore.initNewFeature();
			player.setCombatSpiritStorage(cStore);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getCombatSpiritStorage()!= null) {
			ent.setCombatSpiritJson(JsonUtils.object2String(player.getCombatSpiritStorage()));
		}
	}
}
