package com.mmorpg.mir.model.purse.model;

public class Currency {

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param alter
	 *            变更值(增加正数，扣减负数)
	 * @param current
	 *            当前值(变更之后的值)
	 * @return
	 */
	public static Currency valueOf(CurrencyType type, int alter, long current) {
		Currency result = new Currency();
		result.type = type;
		result.alter = alter;
		result.current = current;
		return result;
	}

	/** 类型 */
	private CurrencyType type;
	/** 变更值(增加正数，扣减负数) */
	private int alter;
	/** 当前值(变更之后的值) */
	private long current;

	private Currency() {
	}

	public CurrencyType getType() {
		return type;
	}

	public int getAlter() {
		return alter;
	}

	public long getCurrent() {
		return current;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

}
