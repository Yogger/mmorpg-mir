package com.mmorpg.mir.model.temple.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class TempleHistoryInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.TEMPLE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getTempleHistoryJson() == null) {
			ent.setTempleHistoryJson(JsonUtils.object2String(new TempleHistory()));
		}

		Player player = ent.getPlayer();

		if (player.getTempleHistory() == null) {
			TempleHistory templeHistory = JsonUtils.string2Object(ent.getTempleHistoryJson(), TempleHistory.class);
			templeHistory.setPlayer(player);
			player.setTempleHistory(templeHistory);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getTempleHistory() != null) {
			ent.setTempleHistoryJson(JsonUtils.object2String(player.getTempleHistory()));
		}
	}

}
