package com.mmorpg.mir.model.boss.model;

public enum BossStatus {
	/** 存活 */
	SURVIVAL(1),
	/** 死亡 */
	DIE(2);

	private int value;

	public static BossStatus valueOf(int code) {
		for (BossStatus id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of BossStatus[" + code + "]");
	}

	private BossStatus(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
