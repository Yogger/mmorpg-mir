package com.mmorpg.mir.model.countrycopy.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CountryCopyInfoInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COUNTRYCOPY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getCountryCopyInfoJson() == null) {
			CountryCopyInfo info = new CountryCopyInfo();
			ent.setCountryCopyInfoJson(JsonUtils.object2String(info));
		}
		
		Player player = ent.getPlayer();
		
		if (player.getCountryCopyInfo() == null) {
			CountryCopyInfo info = JsonUtils.string2Object(ent.getCountryCopyInfoJson(), CountryCopyInfo.class);
			player.setCountryCopyInfo(info);
			info.setOwner(player);
		}
		
	}
	
	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getCountryCopyInfo() != null) {
			ent.setCountryCopyInfoJson(JsonUtils.object2String(player.getCountryCopyInfo()));
		}
	}

}
