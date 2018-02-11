package com.mmorpg.mir.model.boss.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PlayerBossDataInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.BOSS;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getBossJson() == null) {
			ent.setBossJson(JsonUtils.object2String(new PlayerBossData()));
		}

		Player player = ent.getPlayer();

		if (player.getBossData() == null) {
			PlayerBossData pbd = JsonUtils.string2Object(ent.getBossJson(), PlayerBossData.class);
			pbd.init(player);
			player.setBossData(pbd);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getBossData() != null) {
			ent.setBossJson(JsonUtils.object2String(player.getBossData()));
		}
	}

}
