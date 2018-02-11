package com.mmorpg.mir.model.welfare.model;

public enum ClawbackType {

	/** 资源追回 */
	CLAWBACK_TYPE_VALUE(1),

	/** 次数追回 */
	CLAWBACK_TYPE_NUM(2);

	private final int type;

	private ClawbackType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
