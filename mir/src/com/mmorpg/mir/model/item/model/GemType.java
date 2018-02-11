package com.mmorpg.mir.model.item.model;

import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public enum GemType {

	ATTACK(1) {
		@Override
		public boolean isBelongGroup(EquipmentType type) {
			Integer[] group = ItemManager.getInstance().getGemAttackEquipGroup();
			if (group == null) {
				return true;
			} else {
				for (int i = 0; i < group.length; i++) {
					if (group[i] == type.getIndex()) {
						return true;
					}
				}
			}
			return false;
		}
	},
	
	DEFENSE(2) {
		@Override
		public boolean isBelongGroup(EquipmentType type) {
			Integer[] group = ItemManager.getInstance().getGemDefenseEquipGroup();
			if (group == null) {
				return true;
			} else {
				for (int i = 0; i < group.length; i++) {
					if (group[i] == type.getIndex()) {
						return true;
					}
				}
			}
			return false;
		}
	};
	
	private final int value;
	
	private GemType(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return value;
	}
	
	public abstract boolean isBelongGroup(EquipmentType type);
	
}
