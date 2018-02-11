package com.mmorpg.mir.model.warbook.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class WarbookInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.WARBOOK;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getWarBookJson() == null) {
			ent.setWarBookJson(JsonUtils.object2String(Warbook.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getWarBook() == null) {
			Warbook warBook = JsonUtils.string2Object(ent.getWarBookJson(), Warbook.class);
			warBook.setOwner(player);
			player.setWarBook(warBook);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();

		if (player.getWarBook() != null) {
			ent.setWarBookJson(JsonUtils.object2String(player.getWarBook()));
		}
	}
}
