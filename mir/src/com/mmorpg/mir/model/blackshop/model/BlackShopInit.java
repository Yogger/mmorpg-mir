package com.mmorpg.mir.model.blackshop.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.utility.JsonUtils;

@Component
public class BlackShopInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.BLACKSHOP;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getBlackShopJson() == null) {
			ent.setBlackShopJson(JsonUtils.object2String(BlackShop.valueOf(ServerState.getInstance()
					.getBlackShopServer())));
		}

		Player player = ent.getPlayer();

		if (player.getBlackShop() == null) {
			BlackShop blackShop = JsonUtils.string2Object(ent.getBlackShopJson(), BlackShop.class);
			blackShop.setOwner(player);
			player.setBlackShop(blackShop);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getBlackShop() != null) {
			ent.setBlackShopJson(JsonUtils.object2String(player.getBlackShop()));
		}
	}
}
