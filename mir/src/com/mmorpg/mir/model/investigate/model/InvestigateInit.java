package com.mmorpg.mir.model.investigate.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class InvestigateInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.INVESTIGATE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getInvestigateJson() == null) {
			ent.setInvestigateJson(JsonUtils.object2String(new Investigate()));
		}

		Player player = ent.getPlayer();

		if (player.getInvestigate() == null) {
			Investigate investigate = JsonUtils.string2Object(ent.getInvestigateJson(), Investigate.class);
			investigate.setPlayer(player);
			player.setInvestigate(investigate);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getInvestigate() != null) {
			ent.setInvestigateJson(JsonUtils.object2String(player.getInvestigate()));
		}
	}

}
