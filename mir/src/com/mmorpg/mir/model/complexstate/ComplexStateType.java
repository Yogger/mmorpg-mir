package com.mmorpg.mir.model.complexstate;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;

public enum ComplexStateType {
	/** 禁止交易, true表示禁止 */
	TRADE(1),
	/** 禁止组队 */
	GROUP(2),
	/** 是否允许好友 */
	FRIEND(3),
	/** 是否允许决斗 */
	DUEL(4),
	/** 是否禁止他人邀请自己加家族, true表示禁止 */
	GUILD(5),
	/** 坐骑, true表示上马 */
	RIDE(6),
	/** 是否自动接受邀请加入家族 */
	GANG_INVITE(7),
	/** 是否显示神兵, true表示显示 */
	SHOW_ARTIFACT(8),
	/** 是否显示英魂, true表示显示 */
	SHOW_SOUL(9),
	/** 当我是队长时，自动同意他人入队 */
	AUTO_AGREED_JOIN(10),
	/** 当我没有队伍时，自动接受他人的组队邀请 */
	AUTO_ACCEPT_INVITE(11),
	/** 自动升级护符 */
	AUTO_UPGRADE_PROTECTURE(12);

	private int value;

	private ComplexStateType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}

	public static ComplexStateType valueOf(int v) {
		for (ComplexStateType t : values()) {
			if (t.getValue() == v)
				return t;
		}

		throw new ManagedException(ManagedErrorCode.ERROR_MSG);
	}
}
