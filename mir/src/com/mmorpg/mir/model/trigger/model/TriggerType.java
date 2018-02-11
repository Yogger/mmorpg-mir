package com.mmorpg.mir.model.trigger.model;

import java.util.HashMap;
import java.util.Map;

public enum TriggerType {
	/** 刷怪 */
	MONSTER(0),
	/** 释放技能 */
	SKILL(1),
	/** 离开副本 */
	LEAVE_COPY(2),
	/** 地图怪物停止重生 */
	MONSTER_STOP_SPAWN(3),
	/** 任务 */
	QUEST(4),
	/** 传送 */
	TANSPORT(5),
	/** 发一份奖励 */
	REWARD(6),
	/** 副本完成 */
	COPY_COMPLETE(7),
	/** 镖车任务 */
	GUARD(8),
	/** 停止离开副本 */
	STOP_LEAVE_COPY(9),
	/** 军衔等级 */
	MILITARY_RANK(10),
	/** 生成任务怪(目前是JJC专用) */
	QUEST_MONSTER(11),
	/** 设置状态NPC */
	SET_STATUSNPC(12),
	/** 进入副本 */
	ENTER_COPY(13),
	/** 发邮件 */
	MAIL(14),
	/** 发公告 */
	NOTICE(15),
	/** 大表情 */
	EMOTION(16),
	/** 删除召唤物 */
	DELETE_SUMMON(17);

	private static final Map<Integer, TriggerType> types = new HashMap<Integer, TriggerType>(
			TriggerType.values().length);

	static {
		for (TriggerType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public static TriggerType valueOf(int value) {
		TriggerType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的流通货币类型[" + value + "]");
		}
		return result;
	}

	public static TriggerType typeOf(String name) {
		for (TriggerType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的流通货币类型[" + name + "]");
	}

	private final int value;

	private TriggerType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
