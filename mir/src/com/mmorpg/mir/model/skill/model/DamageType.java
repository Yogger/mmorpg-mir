package com.mmorpg.mir.model.skill.model;

import com.windforce.common.utility.JsonUtils;

public enum DamageType {
	/** 普通 */
	NORMAL(0) {
		@Override
		public long calcDamage(long damage, long rate) {
			return damage;
		}
	},
	/** 暴击 */
	CRITICAL(1 << 0) {
		@Override
		public long calcDamage(long damage, long rate) {
			return (long) (Math.ceil(damage * (rate*1.0/10000)));
		}
	},
	/** 破击 */
	IGNORE(1 << 1) {
		@Override
		public long calcDamage(long damage, long rate) {
			return (long) (Math.ceil(damage * 3.0));
		}
	};

	public static void main(String[] args) {
		DamageType[] types = new DamageType[2];
		types[0] = CRITICAL;
		types[1] = IGNORE;
		System.out.println(JsonUtils.object2String(types));
	}

	private byte value;

	public static DamageType valueOf(byte value) {
		for (DamageType type : values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + value + "]");
	}

	private DamageType(int type) {
		value = (byte) type;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public abstract long calcDamage(long damage, long rate);

}
