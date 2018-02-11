package com.mmorpg.mir.model.artifact.core;

import com.mmorpg.mir.model.artifact.resource.ArtifactResource;
import com.mmorpg.mir.model.gameobjects.Player;

public interface IArtifactManager {
	long getIntervalTime();

	ArtifactResource getArtifactResource(int level);

	boolean isOpen(Player player);
}
