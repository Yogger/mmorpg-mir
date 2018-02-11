package com.mmorpg.mir.model.openactive.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class OpenActiveInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.OPENACTIVE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getOpenActiveJson() == null) {
			ent.setOpenActiveJson(JsonUtils.object2String(OpenActive.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getOpenActive() == null) {
			OpenActive active = JsonUtils.string2Object(ent.getOpenActiveJson(), OpenActive.class);
			if (active.getGiftActive() == null) {
				active.setGiftActive(GiftActive.valueOf());
			}
			if (active.getSoulActive() == null) {
				active.setSoulActive(SoulActive.valueOf());
			}
			if (active.getOldSoulActive() == null) {
				active.setOldSoulActive(OldSoulActive.valueOf());
			}
			if (active.getCountryHeroActive() == null) {
				active.setCountryHeroActive(CountryHeroActive.valueOf());
			}
			
			active.getEveryDayRecharge().setOwner(player);
			player.setOpenActive(active);
			player.getOpenActive().registerCompeteRankTypeActivity();
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getOpenActive() != null) {
			ent.setOpenActiveJson(JsonUtils.object2String(player.getOpenActive()));
		}
	}

}
