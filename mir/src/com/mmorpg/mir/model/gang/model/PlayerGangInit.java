package com.mmorpg.mir.model.gang.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.model.PlayerGang;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PlayerGangInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.GANG;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getPlayerGangJson() == null) {
			ent.setPlayerGangJson(JsonUtils.object2String(PlayerGang.valueOf(0)));
		}

		Player player = ent.getPlayer();

		if (player.getPlayerGang() == null) {
			PlayerGang PlayerGang = JsonUtils.string2Object(ent.getPlayerGangJson(), PlayerGang.class);
			player.setPlayerGang(PlayerGang);
			player.getPlayerGang().setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getPlayerGang() != null) {
			ent.setPlayerGangJson(JsonUtils.object2String(player.getPlayerGang()));
		}
	}

}
