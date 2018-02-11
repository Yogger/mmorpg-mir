package com.mmorpg.mir.model.operator.model;

public enum GmPrivilegeType {
	/** 隐身 */
	HIDE(1),
	/** 称号 */
	NICKNAME(2),
	/** 追踪玩家 */
	TRACE(3),
	/** 封号 */
	BAN(4),
	/** 禁言 */
	FORBID_CHAT(5),
	/** 提下线 */
	KICK(6);

	private int value;

	public static GmPrivilegeType valueOf(int code) {
		for (GmPrivilegeType id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of GmPrivilegeType[" + code + "]");
	}

	private GmPrivilegeType(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
