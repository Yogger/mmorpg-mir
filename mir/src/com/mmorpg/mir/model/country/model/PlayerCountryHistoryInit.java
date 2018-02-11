package com.mmorpg.mir.model.country.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.capturetown.model.PlayerCaptureTownInfo;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PlayerCountryHistoryInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COUNTRY_PLAYER_INFO;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getCountryInfoJson() == null) {
			ent.setCountryInfoJson(JsonUtils.object2String(PlayerCountryHistory.valueOf(ent.getPlayer())));
		}
		Player player = ent.getPlayer();
		if (player.getPlayerCountryHistory() == null) {
			PlayerCountryHistory history = JsonUtils
					.string2Object(ent.getCountryInfoJson(), PlayerCountryHistory.class);
			if (history.getCaptureTownInfo() == null) {
				history.setCaptureTownInfo(new PlayerCaptureTownInfo());
			}
			player.setPlayerCountryHistory(history);
			history.setOwner(player);
			history.getCaptureTownInfo().setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getPlayerCountryHistory() != null) {
			ent.setCountryInfoJson(JsonUtils.object2String(player.getPlayerCountryHistory()));
		}
	}

}
