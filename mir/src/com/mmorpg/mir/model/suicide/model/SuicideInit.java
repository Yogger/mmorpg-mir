package com.mmorpg.mir.model.suicide.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class SuicideInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.SUICIDE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getSuicideJson() == null) {
			ent.setSuicideJson(JsonUtils.object2String(Suicide.valueOf()));
		}
		Player player = ent.getPlayer();
		if (player.getSuicide() == null) {
			Suicide suicide = JsonUtils.string2Object(ent.getSuicideJson(), Suicide.class);
			suicide.setOwner(player);
			player.setSuicide(suicide);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getSuicide() != null) {
			ent.setSuicideJson(JsonUtils.object2String(player.getSuicide()));
		}
	}

}
