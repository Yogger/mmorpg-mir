package com.mmorpg.mir.model.lifegrid.packet;

public class SM_LifeGrid_AddEquipStorageSize {
	private int packSize;

	public static SM_LifeGrid_AddEquipStorageSize valueOf(int packSize) {
		SM_LifeGrid_AddEquipStorageSize result = new SM_LifeGrid_AddEquipStorageSize();
		result.packSize = packSize;
		return result;
	}

	public int getPackSize() {
		return packSize;
	}

	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}

}
