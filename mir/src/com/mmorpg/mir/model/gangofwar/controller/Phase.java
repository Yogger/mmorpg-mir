package com.mmorpg.mir.model.gangofwar.controller;

public enum Phase {
	/** 攻击方 */
	ATTACK(1),
	/** 防守方 */
	DEFEND(2);

	private int value;

	public static Phase valueOf(int code) {
		for (Phase id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of Camps[" + code + "]");
	}

	private Phase(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
