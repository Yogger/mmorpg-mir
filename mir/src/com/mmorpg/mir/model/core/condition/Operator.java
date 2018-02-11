package com.mmorpg.mir.model.core.condition;

public enum Operator {
	GREATER(">"), GREATER_EQUAL(">="), EQUAL("="), LESS("<"), LESS_EQUAL("<="), NOT("!");

	private String op;

	private Operator(String op) {
		this.op = op;
	}

	public static Operator typeOf(String op) {
		for (Operator operator : Operator.values()) {
			if (operator.getOp().equals(op)) {
				return operator;
			}
		}
		throw new IllegalArgumentException("非法运算符：" + op);
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}
