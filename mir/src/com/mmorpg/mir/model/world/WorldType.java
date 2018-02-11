package com.mmorpg.mir.model.world;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ATracer
 *
 */
public enum WorldType
{
	NORMAL(0),
	ASMODAE(1),
	ELYSEA(2),
	ABYSS(3),
	PRISON(4),
	NONE(5);
	
	
	private final int value;
	
	private WorldType(int v) {
		this.value = v;
	}
	
	private static final Map<Integer, WorldType> types = new HashMap<Integer, WorldType>(WorldType.values().length);
	
	
	static {
		for (WorldType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public int getValue() {
		return value;
	}

	public static WorldType valueOf(int value) {
		WorldType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的无效的地图类型[" + value + "]");
		}
		return result;
	}

	public static WorldType typeOf(String name) {
		for (WorldType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的地图类型[" + name + "]");
	}
	
}
