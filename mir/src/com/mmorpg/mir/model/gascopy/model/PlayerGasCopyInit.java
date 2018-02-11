package com.mmorpg.mir.model.gascopy.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PlayerGasCopyInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.GASCOPY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getGasCopyJson() == null) {
			ent.setGasCopyJson(JsonUtils.object2String(PlayerGasCopy.valueOf()));
		}
		
		Player player = ent.getPlayer();
		
		if (player.getGasCopy() == null) {
			PlayerGasCopy gasCopy = JsonUtils.string2Object(ent.getGasCopyJson(), PlayerGasCopy.class);
			gasCopy.setOwner(player);
			player.setGasCopy(gasCopy);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		if (ent.getPlayer().getGasCopy() != null) {
			ent.setGasCopyJson(JsonUtils.object2String(ent.getPlayer().getGasCopy()));
		}
	}

}
