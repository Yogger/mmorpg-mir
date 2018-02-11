package com.mmorpg.mir.model.world.resource;

public enum MapCountry {
	/** 中立的 */
	NEUTRAL(0),
	/** 国家1 */
	COUNTRY1(1),
	/** 国家2 */
	COUNTRY2(2),
	/** 国家3 */
	COUNTRY3(3),
	/** 国家4 */
	COUNTRY4(4),
	/** 国家5 */
	COUNTRY5(5),
	/** 国家6 */
	COUNTRY6(6);

	public static MapCountry valueOf(int code) {
		for (MapCountry id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of MapCountry[" + code + "]");
	}

	private final int value;

	private MapCountry(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}
}
