package com.mmorpg.mir.model.artifact.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class ArtifactInit extends ModuleHandle {
	@Override
	public ModuleKey getModule() {
		return ModuleKey.ARTIFACT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getArtifactJson() == null) {
			ent.setArtifactJson(JsonUtils.object2String(Artifact.valueOf(ent.getPlayer())));
		}

		Player player = ent.getPlayer();

		if (player.getArtifact() == null) {
			Artifact yh = JsonUtils.string2Object(ent.getArtifactJson(), Artifact.class);
			if (yh.getEnhanceItemCount() == null) {
				yh.setEnhanceItemCount(new HashMap<String, Integer>());
			}
			if (yh.getGrowItemCount() == null) {
				yh.setGrowItemCount(new HashMap<String, Integer>());
			}
			yh.setOwner(player);
			player.setArtifact(yh);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player != null && player.getArtifact() != null) {
			ent.setArtifactJson(JsonUtils.object2String(player.getArtifact()));
		}
	}
}
