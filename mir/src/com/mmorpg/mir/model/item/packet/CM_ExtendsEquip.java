package com.mmorpg.mir.model.item.packet;

public class CM_ExtendsEquip {

	private long mainEquipIndex;
	private long viceEquipIndex;

	private boolean selectElement;
	private boolean selectSoul;
	private boolean selectRare;

	private int equipStorageType;

	public long getMainEquipIndex() {
		return mainEquipIndex;
	}

	public void setMainEquipIndex(long mainEquipIndex) {
		this.mainEquipIndex = mainEquipIndex;
	}

	public long getViceEquipIndex() {
		return viceEquipIndex;
	}

	public void setViceEquipIndex(long viceEquipIndex) {
		this.viceEquipIndex = viceEquipIndex;
	}

	public boolean isSelectElement() {
		return selectElement;
	}

	public void setSelectElement(boolean selectElement) {
		this.selectElement = selectElement;
	}

	public boolean isSelectSoul() {
		return selectSoul;
	}

	public void setSelectSoul(boolean selectSoul) {
		this.selectSoul = selectSoul;
	}

	public boolean isSelectRare() {
		return selectRare;
	}

	public void setSelectRare(boolean selectRare) {
		this.selectRare = selectRare;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
