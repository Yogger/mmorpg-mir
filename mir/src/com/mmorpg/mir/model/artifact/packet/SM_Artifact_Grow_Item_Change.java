package com.mmorpg.mir.model.artifact.packet;

import java.util.Map;

import com.mmorpg.mir.model.artifact.model.Artifact;

public class SM_Artifact_Grow_Item_Change {
	private Map<String, Integer> growItemCount;

	public static SM_Artifact_Grow_Item_Change valueOf(Artifact artifact) {
		SM_Artifact_Grow_Item_Change result = new SM_Artifact_Grow_Item_Change();
		result.growItemCount = artifact.getGrowItemCount();
		return result;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}
}
