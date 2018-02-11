package com.mmorpg.mir.model.controllers.stats;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class LifeStateInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.LIFE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getLifeJson() == null) {
			ent.setLifeJson(JsonUtils.object2String(new LifeStatDB(1000, 1000, false, 0, 0, 0, 0, "", 0l, 0l)));
		}

		Player player = ent.getPlayer();

		if (player.getLifeStats() == null) {
			LifeStatDB lifeStatDB = JsonUtils.string2Object(ent.getLifeJson(), LifeStatDB.class);
			player.setLifeStats(new PlayerLifeStats(player, lifeStatDB));
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getLifeStats() != null) {
			LifeStatDB db = player.getLifeStats().createLifeStatDB();
			ent.setLifeJson(JsonUtils.object2String(db));
		}
	}

}
