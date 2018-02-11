package com.mmorpg.mir.model.player.model;

/**
 * 账号状态状态
 * 
 * @author Kuang Hao
 * @since v1.0 2012-4-19
 * 
 */
public enum AccountStatus {

	/** 离线状态 */
	UNLINE(0),
	/** 在线状态 */
	ONLINE(1),
	/** 禁止登陆 */
	BAN(2);

	private final int value;

	private AccountStatus(int value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
	}

	/**
	 * Return the enum constant of this type with the specified numeric value.
	 * 
	 * @param statusCode
	 *            the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException
	 *             if this enum has no constant for the specified numeric value
	 */
	public static AccountStatus valueOf(int statusCode) {
		for (AccountStatus status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

}
