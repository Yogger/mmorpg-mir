package com.mmorpg.mir.model.copy.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CopyInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COPY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getCopyJson() == null) {
			ent.setCopyJson(JsonUtils.object2String(new CopyHistory()));
		}

		Player player = ent.getPlayer();

		if (player.getCopyHistory() == null) {
			CopyHistory copyHistory = JsonUtils.string2Object(ent.getCopyJson(), CopyHistory.class);
			copyHistory.setOwner(player);
			player.setCopyHistory(copyHistory);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getCopyHistory() != null) {
			ent.setCopyJson(JsonUtils.object2String(player.getCopyHistory()));
		}
	}
}
