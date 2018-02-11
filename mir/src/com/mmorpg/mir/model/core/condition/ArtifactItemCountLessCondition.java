package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.resource.ArtifactGrowItemResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class ArtifactItemCountLessCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		int count = 0;
		if (player.getArtifact().getGrowItemCount().containsKey(this.code)) {
			count = player.getArtifact().getGrowItemCount().get(this.code);
		}
		ArtifactGrowItemResource resource = ArtifactManager.getInstance().artifactGrowItemStorage.get(this.code, true);

		int maxCount = resource.getItemCountLimit().get(player.getArtifact().getLevel() + "");
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.ARTIFACT_ITEM_COUNT_LIMIT);
	}

}
