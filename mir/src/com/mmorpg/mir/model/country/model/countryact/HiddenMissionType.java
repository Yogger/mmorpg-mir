package com.mmorpg.mir.model.country.model.countryact;

/**
 * 隐藏任务
 * 
 * @author 37wan
 */
public enum HiddenMissionType {
	/** 守卫国旗 */
	DEFEND_FLAG(1),
	/** 推倒国旗 *//*
	CUT_FLAG_DOWN(2),*/
	/** 守卫大臣 */
	DEFEND_DIPOMACY(2),
	/** 推倒大臣 */
	CUT_DIPOMACY_DOWN(3),
	/** 打劫搬砖 */
	ROB_BRICK_MAN(4),
	/** 组队劫镖 */
	ROB_LORRY_GROUP(5),
	/** 个人劫镖 */
	ROB_LORRY_ALONE(6),
	/** 营救 */
	RESCUE(7);

	public static HiddenMissionType valueOf(int v) {
		for (HiddenMissionType type : HiddenMissionType.values()) {
			if (type.getValue() == v) {
				return type;
			}
		}

		throw new IllegalArgumentException(
				"HiddenMissionType valueOf() method params error type[" + v
						+ "]");
	}

	private final int value;

	HiddenMissionType(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

}
