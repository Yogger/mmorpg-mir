package com.mmorpg.mir.model.invest.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class InvestPoolInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.INVEST;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getInvestJson() == null) {
			ent.setInvestJson(JsonUtils.object2String(InvestPool.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getInvestPool() == null) {
			InvestPool pool = JsonUtils.string2Object(ent.getInvestJson(), InvestPool.class);
			player.setInvestPool(pool);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getInvestPool() != null) {
			ent.setInvestJson(JsonUtils.object2String(player.getInvestPool()));
		}
	}
}
