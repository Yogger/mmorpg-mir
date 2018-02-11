package com.mmorpg.mir.model.object.cooldown;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CoolDownInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COOLDOWN;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		Player player = ent.getPlayer();

		if (ent.getCoolDownJson() == null) {
			player.getPlayerEnt().setCoolDownJson(JsonUtils.object2String(player.getCoolDownContainer()));
		}

		if (player.getCoolDownContainer() == null) {
			player.setCoolDownContainer(JsonUtils.string2Object(ent.getCoolDownJson(), CoolDownContainer.class));
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();

		if (player.getCoolDownContainer() != null) {
			ent.setCoolDownJson(JsonUtils.object2String(player.getCoolDownContainer()));
		}
	}
}
