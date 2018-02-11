package com.mmorpg.mir.model.copy.model;

import java.util.HashMap;
import java.util.Map;

public enum CopyType {
	/** 任务剧情副本 */
	QUEST(0),
	/** 经验副本 */
	EXP(1),
	/** VIP副本 */
	VIP(2),
	/** 爬塔 */
	LADDER(3),
	/** 军衔 */
	MILITARY(4),
	/** 个人BOSS副本 */
	BOSS(5),
	/** 名将副本 */
	MINGJIANG(6),
	/** 兵书副本 */
	WARBOOK(7),
	/** 坐骑装备副本 */
	HORSEEQUIP(8);

	private static final Map<Integer, CopyType> types = new HashMap<Integer, CopyType>(CopyType.values().length);

	static {
		for (CopyType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public static CopyType valueOf(int value) {
		CopyType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的副本类型[" + value + "]");
		}
		return result;
	}

	public static CopyType typeOf(String name) {
		for (CopyType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的副本类型[" + name + "]");
	}

	private final int value;

	private CopyType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
