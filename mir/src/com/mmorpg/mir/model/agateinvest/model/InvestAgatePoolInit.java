package com.mmorpg.mir.model.agateinvest.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class InvestAgatePoolInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.INVESTAGATE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if(ent.getInvestAgateJson()== null){
			ent.setInvestAgateJson(JsonUtils.object2String(InvestAgatePool.valueOf()));
		}
		Player player = ent.getPlayer();
		if (player.getInvestAgatePool() == null) {
			InvestAgatePool pool = JsonUtils.string2Object(ent.getInvestAgateJson(), InvestAgatePool.class);
			player.setInvestAgatePool(pool);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getInvestAgatePool() != null) {
			ent.setInvestAgateJson(JsonUtils.object2String(player.getInvestAgatePool()));
		}
	}
}
