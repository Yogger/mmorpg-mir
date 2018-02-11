package com.mmorpg.mir.model.gameobjects;

public abstract class JourObject {

	protected Long objectId;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public abstract String getName();

}
