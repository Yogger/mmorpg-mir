package com.mmorpg.mir.model.artifact.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Artifact_EnhanceCount_Change  {
	
	private HashMap<String, Integer> enhanceItemCount;

	public static SM_Artifact_EnhanceCount_Change valueOf(Player player) {
		SM_Artifact_EnhanceCount_Change result = new SM_Artifact_EnhanceCount_Change();
		result.enhanceItemCount = new HashMap<String, Integer>(player.getArtifact().getEnhanceItemCount());
		return result;
	}

	public HashMap<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(HashMap<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}
	
}
