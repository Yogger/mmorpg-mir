package com.mmorpg.mir.model.moduleopen.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class ModuleOpenInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.MODULE_OPEN_INFO;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getModuleOpenJson() == null) {
			ent.setModuleOpenJson(JsonUtils.object2String(ModuleOpen.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getModuleOpen() == null) {
			ModuleOpen moduleOpen = JsonUtils.string2Object(ent.getModuleOpenJson(), ModuleOpen.class);
			moduleOpen.initIfNeed();
			player.setModuleOpen(moduleOpen);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getModuleOpen() != null) {
			ent.setModuleOpenJson(JsonUtils.object2String(player.getModuleOpen()));
		}
	}
}
