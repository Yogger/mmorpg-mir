package com.mmorpg.mir.model.complexstate;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;

public enum ServerComplexStateType {
	/** 坐骑 */
	RIDE(1);

	private int value;

	private ServerComplexStateType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}

	public static ServerComplexStateType valueOf(int v) {
		for (ServerComplexStateType t : values()) {
			if (t.getValue() == v)
				return t;
		}

		throw new ManagedException(ManagedErrorCode.ERROR_MSG);
	}
}
