package com.mmorpg.mir.model.exchange.packet;

public class CM_Exchange_Add_Currency {

	private int currencyType;
	private int amount;

	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
