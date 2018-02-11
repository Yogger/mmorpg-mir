package com.mmorpg.mir.model.relive.manager;

import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.windforce.common.resource.Storage;

public interface IPlayerReliveManager {
	public int getAutoReliveCD();

	public Storage<Integer, ReliveBaseResource> getReliveBaseResources();

	public void setReliveBaseResources(Storage<Integer, ReliveBaseResource> reliveBaseResources);

	public ReliveBaseResource getReliveResource(int reliveId);

	public String getChooserGroupId(int reliveId);

}
