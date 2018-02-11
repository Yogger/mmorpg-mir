package com.mmorpg.mir.model.country.model;

public enum OfficalCtxKey {
	/** 最后一次召集国民的时间 */
	CALLTOGETHER_CD_END(1);

	private int value;

	public static OfficalCtxKey valueOf(int code) {
		for (OfficalCtxKey id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private OfficalCtxKey(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
