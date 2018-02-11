package com.mmorpg.mir.model.warship.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class WarshipInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.WARSHIP;
	}
	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getWarshipJson() == null) {
			Warship warship = new Warship();
			ent.setWarshipJson(JsonUtils.object2String(warship));
		}
		
		Player player = ent.getPlayer();
		
		if (player.getWarship() == null) {
			Warship m = JsonUtils.string2Object(ent.getWarshipJson(), Warship.class);
			player.setWarship(m);
		}
		
	}
	
	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getWarship() != null) {
			ent.setWarshipJson(JsonUtils.object2String(player.getWarship()));
		}
	}
}
