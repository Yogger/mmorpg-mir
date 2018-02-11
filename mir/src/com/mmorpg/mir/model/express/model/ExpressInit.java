package com.mmorpg.mir.model.express.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class ExpressInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.EXPRESS;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getExpressJson() == null) {
			ent.setExpressJson(JsonUtils.object2String(Express.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getExpress() == null) {
			Express express = JsonUtils.string2Object(ent.getExpressJson(), Express.class);
			express.setOwner(player);
			player.setExpress(express);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getExpress() != null) {
			ent.setExpressJson(JsonUtils.object2String(player.getExpress()));
		}
	}

}
