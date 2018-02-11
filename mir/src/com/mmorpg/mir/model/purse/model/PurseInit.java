package com.mmorpg.mir.model.purse.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PurseInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.PURSE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getPurseJson() == null) {
			ent.setPurseJson(JsonUtils.object2String(Purse.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getPurse() == null) {
			Purse purse = JsonUtils.string2Object(ent.getPurseJson(), Purse.class);
			player.setPurse(purse);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getPurse() != null) {
			ent.setPurseJson(JsonUtils.object2String(player.getPurse()));
		}
	}
}
