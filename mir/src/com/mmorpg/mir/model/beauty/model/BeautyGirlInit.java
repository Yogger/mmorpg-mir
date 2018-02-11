package com.mmorpg.mir.model.beauty.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class BeautyGirlInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.BEAUTY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getBeautyGirlJson() == null) {
			ent.setBeautyGirlJson(JsonUtils.object2String(BeautyGirlPool.valueOf()));
		}

		Player player = ent.getPlayer();
		if (player.getBeautyGirlPool() == null) {
			BeautyGirlPool pool = JsonUtils.string2Object(ent.getBeautyGirlJson(), BeautyGirlPool.class);
			pool.setOwner(player);
			if (pool.getItemCounts() == null) {
				pool.setItemCounts(new HashMap<String, Integer>());
			}
			player.setBeautyGirlPool(pool);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getBeautyGirlPool() != null) {
			ent.setBeautyGirlJson(JsonUtils.object2String(player.getBeautyGirlPool()));
		}
	}
}
