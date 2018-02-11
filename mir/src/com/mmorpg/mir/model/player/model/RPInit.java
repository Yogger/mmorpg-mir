package com.mmorpg.mir.model.player.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class RPInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.RP;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getRpJson() == null) {
			ent.setRpJson(JsonUtils.object2String(RP.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getRp() == null) {
			RP rp = JsonUtils.string2Object(ent.getRpJson(), RP.class);
			player.setRp(rp);
			rp.setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getRp() != null) {
			ent.setRpJson(JsonUtils.object2String(player.getRp()));
		}
	}
}
