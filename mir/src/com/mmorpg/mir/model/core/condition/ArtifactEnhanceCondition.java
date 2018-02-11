package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.resource.ArtifactResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class ArtifactEnhanceCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		Integer count = player.getArtifact().getEnhanceItemCount().get(this.code);

		if (count == null) {
			count = 0;
		}

		ArtifactResource resource = ArtifactManager.getInstance().getArtifactResource(player.getArtifact().getLevel());
		Integer maxCount = resource.getEnhanceItemCount().get(this.code);
		if (maxCount == null) {
			maxCount = 0;
		}

		return count < maxCount;
	}

}
