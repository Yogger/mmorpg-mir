package com.mmorpg.mir.model.artifact.core;

import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Uplevel;
import com.mmorpg.mir.model.gameobjects.Player;

public interface ArtifactService {
	void initArtifactStats(Player player);

	SM_Artifact_Uplevel uplevel(Player player, boolean autoBuy);

	void flushAritfact(Player player, boolean upLevel, boolean clear);

	public void buyArtifactBuff(Player player);

	public void deprechArtifactBuff(Player player);

	public void addBuffStates(Player player);

}
