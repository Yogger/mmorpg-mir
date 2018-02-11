package com.mmorpg.mir.model.country.model;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;

public enum CoppersType {
	/** 国库银两(0) */
	MONEY(0, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY),
	/** 物资1(1) */
	I1(1, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY),
	/** 物资2(2) */
	I2(2, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY),
	/** 物资3(3) */
	I3(3, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY),
	/** 物资4 */
	I4(4, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY),
	/** 国家商店经验值 */
	SHOP_EXP(5, ManagedErrorCode.NOT_ENOUGH_COUNTRY_MOENY);

	private static final Map<Integer, CoppersType> types = new HashMap<Integer, CoppersType>(
			CoppersType.values().length);

	static {
		for (CoppersType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public static CoppersType valueOf(int value) {
		CoppersType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的流通货币类型[" + value + "]");
		}
		return result;
	}

	public static CoppersType typeOf(String name) {
		for (CoppersType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的流通货币类型[" + name + "]");
	}

	private final int value;

	private final int errorCode;

	private CoppersType(int value, int errorCode) {
		this.value = value;
		this.errorCode = errorCode;
	}

	public int getValue() {
		return this.value;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
