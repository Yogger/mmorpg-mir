package com.mmorpg.mir.model.item.model;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;

public enum EquipmentStorageType {
	PLAYER(0) {
		@Override
		public EquipmentStorage getEquipmentStorage(Player player) {
			return player.getEquipmentStorage();
		}
	},

	HORSE(1) {
		@Override
		public EquipmentStorage getEquipmentStorage(Player player) {
			return player.getHorseEquipmentStorage();
		}
	};

	private int where;

	private EquipmentStorageType(int w) {
		this.where = w;
	}

	public static EquipmentStorageType typeOf(int w) {
		for (EquipmentStorageType type : EquipmentStorageType.values()) {
			if (type.getWhere() == w) {
				return type;
			}
		}
		throw new IllegalArgumentException("非法装备栏类型");
	}

	public abstract EquipmentStorage getEquipmentStorage(Player player);

	public int getWhere() {
		return where;
	}

	public void setWhere(int where) {
		this.where = where;
	}

}
