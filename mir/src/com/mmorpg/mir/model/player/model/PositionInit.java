package com.mmorpg.mir.model.player.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;

@Component
public class PositionInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.POSITION;
	}

	@Override
	public void deserialize(PlayerEnt ent) {

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		ent.setX(player.getX());
		ent.setY(player.getY());
		ent.setMapId(player.getMapId());
		ent.setHeading(player.getHeading());
	}
}
