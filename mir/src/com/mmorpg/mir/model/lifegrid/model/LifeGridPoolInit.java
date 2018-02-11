package com.mmorpg.mir.model.lifegrid.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.lifegrid.LifeGridConfig;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class LifeGridPoolInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.LIFEGRID;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getLifeGridJson() == null) {
			ent.setLifeGridJson(JsonUtils.object2String(LifeGridPool.valueOf(
					LifeGridConfig.getInstance().LIFEGRID_EQUIP_PACK_SIZE.getValue(),
					LifeGridConfig.getInstance().LIFEGRID_PACK_SIZE.getValue(),
					LifeGridConfig.getInstance().LIFEGRID_STORAGE_SIZE.getValue())));
		}

		Player player = ent.getPlayer();

		if (player.getLifeGridPool() == null) {
			LifeGridPool pool = JsonUtils.string2Object(ent.getLifeGridJson(), LifeGridPool.class);
			pool.setOwner(player);
			player.setLifeGridPool(pool);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getLifeGridPool() != null) {
			ent.setLifeGridJson(JsonUtils.object2String(player.getLifeGridPool()));
		}
	}

}
