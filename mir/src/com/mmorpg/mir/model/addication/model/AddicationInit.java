package com.mmorpg.mir.model.addication.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class AddicationInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.ANTIADDICATION;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getAddicationJson() == null) {
			ent.setAddicationJson(JsonUtils.object2String(new Addication()));
		}

		Player player = ent.getPlayer();

		if (player.getAddication() == null) {
			Addication addication = JsonUtils.string2Object(ent.getAddicationJson(), Addication.class);
			addication.setOwner(player);
			player.setAddication(addication);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getAddication() != null) {
			ent.setAddicationJson(JsonUtils.object2String(player.getAddication()));
		}
	}

}
