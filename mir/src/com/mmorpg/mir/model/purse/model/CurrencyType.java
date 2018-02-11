package com.mmorpg.mir.model.purse.model;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;

public enum CurrencyType {

	/** 铜币(0) */
	COPPER(0, ManagedErrorCode.NOT_ENOUGH_COPPER),
	/** 元宝(1) */
	GOLD(1, ManagedErrorCode.NOT_ENOUGH_GOLD),
	/** 礼券(2) */
	GIFT(2, ManagedErrorCode.NOT_ENOUGH_GIFT),
	/** 内币(3) */
	INTER(3, ManagedErrorCode.NOT_ENOUGH_INNER),
	/** 阅历 */
	QI(4, ManagedErrorCode.NOT_ENOUGH_QI),
	/** 贡献 */
	CONTRIBUTION(5, ManagedErrorCode.NOT_ENOUGH_CONTRIBUTION),
	/** 荣誉 */
	HONOR(6, ManagedErrorCode.MILITARY_NOT_ENOUGH),
	/** 黑市积分 */
	BLACKSHOP_POINT(7, ManagedErrorCode.BLACKSHOP_POINT_NOT_ENOUGH), 
	/** 功勋积分 */
	FEATS(8, ManagedErrorCode.FEATS_NOT_ENOUGH),
	/** BOSS积分 */
	BOSS_COINS(9, ManagedErrorCode.BOSS_COINS_NOT_ENOUGH),
	;

	private static final Map<Integer, CurrencyType> types = new HashMap<Integer, CurrencyType>(
			CurrencyType.values().length);

	static {
		for (CurrencyType type : values()) {
			types.put(type.getValue(), type);
		}
	}

	public static CurrencyType valueOf(int value) {
		CurrencyType result = types.get(value);
		if (result == null) {
			throw new IllegalArgumentException("无效的流通货币类型[" + value + "]");
		}
		return result;
	}

	public static CurrencyType typeOf(String name) {
		for (CurrencyType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的流通货币类型[" + name + "]");
	}

	private final int value;

	private final int errorCode;

	private CurrencyType(int value, int errorCode) {
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
