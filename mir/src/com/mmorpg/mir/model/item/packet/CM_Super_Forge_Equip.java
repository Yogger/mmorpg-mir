package com.mmorpg.mir.model.item.packet;

public class CM_Super_Forge_Equip {
	// 装备
	private String equipKey;
	// 五行
	private String elementType;
	// 灵魂属性
	private String equipSoulKey;

	public String getEquipKey() {
		return equipKey;
	}

	public void setEquipKey(String equipKey) {
		this.equipKey = equipKey;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getEquipSoulKey() {
		return equipSoulKey;
	}

	public void setEquipSoulKey(String equipSoulKey) {
		this.equipSoulKey = equipSoulKey;
	}

}
