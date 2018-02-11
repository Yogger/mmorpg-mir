package com.mmorpg.mir.model.soul.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class SoulInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.SOUL_PF;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getSoulJson() == null) {
			ent.setSoulJson(JsonUtils.object2String(Soul.valueOf(ent.getPlayer())));
		}

		Player player = ent.getPlayer();

		if (player.getSoul() == null) {
			Soul yh = JsonUtils.string2Object(ent.getSoulJson(), Soul.class);
			if (yh.getEnhanceItemCount() == null) {
				yh.setEnhanceItemCount(new HashMap<String, Integer>());
			}
			if (yh.getGrowItemCount() == null) {
				yh.setGrowItemCount(new HashMap<String, Integer>());
			}
			yh.setOwner(player);
			player.setSoul(yh);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player != null && player.getSoul() != null) {
			ent.setSoulJson(JsonUtils.object2String(player.getSoul()));
		}
	}

}
