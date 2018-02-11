package com.mmorpg.mir.model.copy.manager;

import com.mmorpg.mir.model.copy.resource.CopyIndividualBossResource;
import com.mmorpg.mir.model.copy.resource.CopyLadderRewardResource;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.windforce.common.resource.Storage;

public interface ICopyManager {
	Storage<String, CopyResource> getCopyResources();

	void setCopyResources(Storage<String, CopyResource> copyResources);

	Storage<String, CopyLadderRewardResource> getCopyeLadderRewardResources();

	void setCopyeLadderRewardResources(Storage<String, CopyLadderRewardResource> copyeLadderRewardResources);

	CopyIndividualBossResource getCopyIndividualBossResource(String id);
}
