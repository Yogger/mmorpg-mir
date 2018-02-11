package com.mmorpg.mir.model.lifegrid.packet;

public class CM_LifeGrid_Devour {
	private int fromStorageType;
	private int toStorageType;
	private int fromIndex;
	private int toIndex;

	public int getFromStorageType() {
		return fromStorageType;
	}

	public void setFromStorageType(int fromStorageType) {
		this.fromStorageType = fromStorageType;
	}

	public int getToStorageType() {
		return toStorageType;
	}

	public void setToStorageType(int toStorageType) {
		this.toStorageType = toStorageType;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}

}
