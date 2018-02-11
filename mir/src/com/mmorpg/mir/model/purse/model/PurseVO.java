package com.mmorpg.mir.model.purse.model;

import java.util.Map;

public class PurseVO {
	private Map<String, Long> currencies;
	private Map<String, Long> totalCurrencies;

	public Map<String, Long> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, Long> currencies) {
		this.currencies = currencies;
	}

	public Map<String, Long> getTotalCurrencies() {
		return totalCurrencies;
	}

	public void setTotalCurrencies(Map<String, Long> totalCurrencies) {
		this.totalCurrencies = totalCurrencies;
	}

}
