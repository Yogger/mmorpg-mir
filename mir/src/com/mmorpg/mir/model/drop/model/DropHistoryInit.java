package com.mmorpg.mir.model.drop.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class DropHistoryInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.DROP;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getDropHistoryJson() == null) {
			ent.setDropHistoryJson(JsonUtils.object2String(new DropHistory()));
		}

		Player player = ent.getPlayer();

		if (player.getDropHistory() == null) {
			DropHistory dropHistory = JsonUtils.string2Object(ent.getDropHistoryJson(), DropHistory.class);
			player.setDropHistory(dropHistory);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getDropHistory() != null) {
			ent.setDropHistoryJson(JsonUtils.object2String(player.getDropHistory()));
		}
	}

}
