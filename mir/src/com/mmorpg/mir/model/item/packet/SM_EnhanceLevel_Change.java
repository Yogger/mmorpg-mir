package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_EnhanceLevel_Change {

	private long objectId;
	/** 全身强化等级 */
	private byte enhanceLevel;

	private int equipStorageType;

	public static SM_EnhanceLevel_Change valueOf(long pid, int enhancelevel, EquipmentStorageType equipStorageType) {
		SM_EnhanceLevel_Change sm = new SM_EnhanceLevel_Change();
		sm.objectId = pid;
		sm.enhanceLevel = (byte) enhancelevel;
		sm.equipStorageType = equipStorageType.getWhere();
		return sm;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public byte getEnhanceLevel() {
		return enhanceLevel;
	}

	public void setEnhanceLevel(byte enhanceLevel) {
		this.enhanceLevel = enhanceLevel;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}
}
