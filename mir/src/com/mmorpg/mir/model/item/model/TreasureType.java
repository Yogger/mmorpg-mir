package com.mmorpg.mir.model.item.model;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.log.SubModuleType;

public enum TreasureType {
	/** 普通 */
	COMMON(1),
	/** 高级 */
	HIGH(2);

	private int value;

	private ModuleKey key;

	private TreasureType(int v) {
		this.value = v;
	}

	public static TreasureType typeOf(int v) {
		for (TreasureType type : TreasureType.values()) {
			if (type.getValue() == v) {
				return type;
			}
		}
		throw new IllegalArgumentException("非法探宝类型");
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ModuleKey getKey() {
		return key;
	}

	public void setKey(ModuleKey key) {
		this.key = key;
	}

	public static SubModuleType getRewardSubType(TreasureType treasureType, int grade) {
		switch (grade) {
		case 1:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_REWARD_ONE
					: SubModuleType.TREASURE_HIGH_SEEK_REWARD_ONE;
		case 2:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_REWARD_SECOND
					: SubModuleType.TREASURE_HIGH_SEEK_REWARD_SECOND;
		case 3:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_REWARD_THIRD
					: SubModuleType.TREASURE_HIGH_SEEK_REWARD_THIRD;
		}
		return null;
	}

	public static SubModuleType getActSubType(TreasureType treasureType, int grade) {
		switch (grade) {
		case 1:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_ACT_ONE
					: SubModuleType.TREASURE_HIGH_SEEK_ACT_ONE;
		case 2:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_ACT_SECOND
					: SubModuleType.TREASURE_HIGH_SEEK_ACT_SECOND;
		case 3:
			return treasureType == COMMON ? SubModuleType.TREASURE_COMMON_SEEK_ACT_THIRD
					: SubModuleType.TREASURE_HIGH_SEEK_ACT_THIRD;
		}
		return null;
	}
}
