package com.mmorpg.mir.model.item.packet;

import java.util.Map;

public class SM_Sell {
	private int code;
	private Map<Integer, Object> packUpdate;
	private Map<Integer, Object> buyBackUpdate;
	private Map<Integer, Long> currencyUpdate;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public Map<Integer, Object> getBuyBackUpdate() {
		return buyBackUpdate;
	}

	public void setBuyBackUpdate(Map<Integer, Object> buyBackUpdate) {
		this.buyBackUpdate = buyBackUpdate;
	}

	public Map<Integer, Long> getCurrencyUpdate() {
		return currencyUpdate;
	}

	public void setCurrencyUpdate(Map<Integer, Long> currencyUpdate) {
		this.currencyUpdate = currencyUpdate;
	}

}
