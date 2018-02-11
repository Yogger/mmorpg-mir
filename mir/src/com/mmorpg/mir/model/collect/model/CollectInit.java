package com.mmorpg.mir.model.collect.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CollectInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COLLECT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getCollectJson() == null) {
			ent.setCollectJson(JsonUtils.object2String(Collect.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getCollect() == null) {
			Collect collect = JsonUtils.string2Object(ent.getCollectJson(), Collect.class);
			if (collect.getFamedGeneral() == null) {
				collect.setFamedGeneral(FamedGeneral.valueOf());
			}
			player.setCollect(collect);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getCollect() != null) {
			ent.setCollectJson(JsonUtils.object2String(player.getCollect()));
		}
	}

}
