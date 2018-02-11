package com.mmorpg.mir.model.country.model;

public enum CountryId {
	/** 国家1 */
	C1(1),
	/** 国家2 */
	C2(2),
	/** 国家3 */
	C3(3);

	private int value;

	public static CountryId valueOf(int code) {
		for (CountryId id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private CountryId(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
