package com.mmorpg.mir.model.welfare.model;

public enum ActionCurrencyType {

	/** 铜币 */
	ACTION_COPPER(1),

	/** 元宝 */
	ACTION_GOLD(2);

	private final int type;

	private ActionCurrencyType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
