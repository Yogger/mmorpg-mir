package com.mmorpg.mir.model.gangofwar.controller;

public enum Camps {
	/** 攻击方 */
	ATTACK(1),
	/** 防守方 */
	DEFEND(2);

	private int value;

	public static Camps valueOf(int code) {
		for (Camps id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of Camps[" + code + "]");
	}

	private Camps(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
