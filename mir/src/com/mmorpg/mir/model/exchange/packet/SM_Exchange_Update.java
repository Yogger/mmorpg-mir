package com.mmorpg.mir.model.exchange.packet;

import java.util.Map;

public class SM_Exchange_Update {
	private byte type;
	private Map<Integer, Object> exchangeUpdate;
	private Map<Integer, Integer> currencys;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public Map<Integer, Object> getExchangeUpdate() {
		return exchangeUpdate;
	}

	public void setExchangeUpdate(Map<Integer, Object> exchangeUpdate) {
		this.exchangeUpdate = exchangeUpdate;
	}

	public Map<Integer, Integer> getCurrencys() {
		return currencys;
	}

	public void setCurrencys(Map<Integer, Integer> currencys) {
		this.currencys = currencys;
	}

	public static SM_Exchange_Update valueOf(byte type, Map<Integer, Object> exchangeUpdate,
			Map<Integer, Integer> currencys) {
		SM_Exchange_Update req = new SM_Exchange_Update();
		req.type = type;
		req.exchangeUpdate = exchangeUpdate;
		req.currencys = currencys;
		return req;
	}
}
