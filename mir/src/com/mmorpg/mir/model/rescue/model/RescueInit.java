package com.mmorpg.mir.model.rescue.model;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.utility.JsonUtils;

@Component
public class RescueInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.RESCUE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getRescueJson() == null) {
			ent.setRescueJson(JsonUtils.object2String(Rescue.valueOf(ent.getPlayer())));
		}

		Player player = ent.getPlayer();

		if (player.getRescue() == null) {
			Rescue rescue = JsonUtils.string2Object(ent.getRescueJson(), Rescue.class);
			if (!rescue.getItems().isEmpty()) {
				Collections.sort(rescue.getItems());
			}
			if (rescue.getReward() == null) {
				rescue.setReward(Reward.valueOf());
			}
			rescue.setOwner(player);
			player.setRescue(rescue);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getRescue() != null) {
			ent.setRescueJson(JsonUtils.object2String(player.getRescue()));
		}
	}
}
