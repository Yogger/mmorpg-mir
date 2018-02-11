package com.mmorpg.mir.model.player.resource;

public enum Role {
	/** 战士 */
	WARRIOR(1),
	/** 弓手 */
	ARCHER(2),
	/** 谋士 */
	STRATEGIST(3),
	/** 方士 */
	SORCERER(4);

	private final int value;

	private Role(int value) {
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
		return String.valueOf(value);
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
	public static Role valueOf(int statusCode) {
		for (Role status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}
}
