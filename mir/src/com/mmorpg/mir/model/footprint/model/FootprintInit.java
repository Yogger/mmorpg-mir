package com.mmorpg.mir.model.footprint.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class FootprintInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.FOOTPRINT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getFootprintPoolJson() == null) {
			ent.setFootprintPoolJson(JsonUtils.object2String(FootprintPool.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getFootprintPool() == null) {
			FootprintPool footprintPool = JsonUtils.string2Object(ent.getFootprintPoolJson(), FootprintPool.class);
			footprintPool.setOwner(player);
			player.setFootprintPool(footprintPool);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getFootprintPool() != null) {
			ent.setFootprintPoolJson(JsonUtils.object2String(player.getFootprintPool()));
		}
	}

}
