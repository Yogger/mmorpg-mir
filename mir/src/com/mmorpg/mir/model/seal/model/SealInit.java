package com.mmorpg.mir.model.seal.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class SealInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.SEAL;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getSealJson() == null) {
			ent.setSealJson(JsonUtils.object2String(Seal.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getSeal() == null) {
			Seal seal = JsonUtils.string2Object(ent.getSealJson(), Seal.class);
			seal.setOwner(player);
			player.setSeal(seal);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();

		if (player.getSeal() != null) {
			ent.setSealJson(JsonUtils.object2String(player.getSeal()));
		}
	}

}
