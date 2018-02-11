package com.mmorpg.mir.model.vip.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class VipInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.VIP;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getVipJson() == null) {
			ent.setVipJson(JsonUtils.object2String(Vip.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getVip() == null) {
			Vip vip = JsonUtils.string2Object(ent.getVipJson(), Vip.class);
			player.setVip(vip);
			vip.setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getVip() != null) {
			ent.setVipJson(JsonUtils.object2String(player.getVip()));
		}
	}
}
