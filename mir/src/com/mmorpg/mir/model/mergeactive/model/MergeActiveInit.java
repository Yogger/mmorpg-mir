package com.mmorpg.mir.model.mergeactive.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class MergeActiveInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.MERGEACTIVE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if(ent.getMergeActiveJson() == null){
			ent.setMergeActiveJson(JsonUtils.object2String(MergeActive.valueOf()));
		}
		Player player = ent.getPlayer();
		if(player.getMergeActive() == null){
			MergeActive mergeActive = JsonUtils.string2Object(ent.getMergeActiveJson(), MergeActive.class);
			if (mergeActive.getConsumeCompete() == null) {
				mergeActive.setConsumeCompete(ConsumeCompete.valueOf());
			}
			
			player.setMergeActive(mergeActive);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if(player.getMergeActive() != null){
			ent.setMergeActiveJson(JsonUtils.object2String(player.getMergeActive()));
		}
	}

}
