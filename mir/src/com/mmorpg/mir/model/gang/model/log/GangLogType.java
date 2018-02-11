package com.mmorpg.mir.model.gang.model.log;

import java.util.HashMap;
import java.util.Map;

/**
 *  族创建记录：记录本家族的创建日期和创建者的名字 家族转让记录：记录每一次家族族长更换的日期和更换者信息
 * 族员加入记录：记录每一个族员的入族时间和入族者名称 族员退出记录：记录每一个族员的退族时间和退族者名称
 * 族员开除记录：记录每一个族员的被开除时间和被开除者名称
 * 族员击杀记录：记录每一个族员在野外地图击杀其他家族玩家的时间，击杀地图坐标，击杀者名称和被击杀者名称
 * 族员被杀记录：记录每一个族员在野外地图被其他家族玩家击杀的时间，击杀地图坐标，击杀者名称和被击杀者名称 页面中的以下信息委可点击超链接信息
 * 玩家名字：点击后弹出该玩家的角色属性界面 地图坐标点：点击后自动寻路至该坐标点
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-10
 * 
 */
public enum GangLogType {

	/** 创建帮会 */
	CREATE(0),
	/** 家族转让 */
	TRANSFER(1),
	/** 加入(2) */
	JOIN(2),
	/** 退出(3) */
	QUIT(3),
	/** 开除 */
	EXPEL(4);

	private static final Map<Integer, GangLogType> types = new HashMap<Integer, GangLogType>(
			GangLogType.values().length);

	static {
		for (GangLogType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public static GangLogType valueOf(int value) {
		GangLogType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的流通货币类型[" + value + "]");
		}
		return result;
	}

	public static GangLogType typeOf(String name) {
		for (GangLogType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的流通货币类型[" + name + "]");
	}

	private final int value;

	private GangLogType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
