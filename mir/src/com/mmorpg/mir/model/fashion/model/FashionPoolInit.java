package com.mmorpg.mir.model.fashion.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class FashionPoolInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.FASHION;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getFashionPoolJson() == null) {
			ent.setFashionPoolJson(JsonUtils.object2String(FashionPool.valueOf()));
		}

		Player player = ent.getPlayer();
		if (player.getFashionPool() == null) {
			FashionPool fashionPool = JsonUtils.string2Object(ent.getFashionPoolJson(), FashionPool.class);
			fashionPool.setOwner(player);
			player.setFashionPool(fashionPool);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getFashionPool() != null) {
			ent.setFashionPoolJson(JsonUtils.object2String(player.getFashionPool()));
		}
	}
}
