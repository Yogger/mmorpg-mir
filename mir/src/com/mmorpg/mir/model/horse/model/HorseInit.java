package com.mmorpg.mir.model.horse.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class HorseInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.HORSE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getHorseJson() == null) {
			ent.setHorseJson(JsonUtils.object2String(Horse.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getHorse() == null) {
			Horse horse = JsonUtils.string2Object(ent.getHorseJson(), Horse.class);
			if (horse.getEnhanceItemCount() == null) {
				horse.setEnhanceItemCount(new HashMap<String, Integer>());
			}
			if (horse.getLearnedSkills() == null) {
				horse.setLearnedSkills(new HashMap<Integer, Integer>());
			}
			if (horse.getGrowItemCount() == null) {
				horse.setGrowItemCount(new HashMap<String, Integer>());
			}
			horse.setOwner(player);
			player.setHorse(horse);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getHorse() != null) {
			ent.setHorseJson(JsonUtils.object2String(player.getHorse()));
		}
	}

}
