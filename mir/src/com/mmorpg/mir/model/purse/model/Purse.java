package com.mmorpg.mir.model.purse.model;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public class Purse {
	private Map<CurrencyType, Long> currencies;

	private Map<CurrencyType, Long> totalCurrencies;

	@JsonIgnore
	private BitSet marks = new BitSet(CurrencyType.values().length);

	public static Purse valueOf() {
		Purse purse = new Purse();
		purse.currencies = New.hashMap();
		purse.totalCurrencies = New.hashMap();
		return purse;
	}

	public PurseVO creatPurseVO() {
		PurseVO vo = new PurseVO();
		vo.setCurrencies(new HashMap<String, Long>());
		for (Entry<CurrencyType, Long> entry : currencies.entrySet()) {
			vo.getCurrencies().put(entry.getKey().name(), entry.getValue());
		}

		vo.setTotalCurrencies(new HashMap<String, Long>());
		for (Entry<CurrencyType, Long> entry : totalCurrencies.entrySet()) {
			vo.getTotalCurrencies().put(entry.getKey().name(), entry.getValue());
		}
		return vo;
	}

	@JsonIgnore
	public long cost(CurrencyType type, int value) {
		long remain = this.getValue(type);
		if (value <= remain) {
			currencies.put(type, remain - value);
			marks.set(type.getValue());
			return 0;
		} else {
			// 不足
			currencies.put(type, 0l);
			marks.set(type.getValue());
			return value - remain;
		}
	}

	@JsonIgnore
	public void add(CurrencyType type, int value) {
		currencies.put(type, this.getValue(type) + value);
		totalCurrencies.put(type, (totalCurrencies.containsKey(type) ? totalCurrencies.get(type) : 0) + value);
		marks.set(type.getValue());
	}

	@JsonIgnore
	public boolean isEnough(CurrencyType type, long value) {
		if (type == CurrencyType.GOLD) {
			// 元宝和内币特殊判断
			if (this.getValue(CurrencyType.GOLD, CurrencyType.INTER) >= value) {
				return true;
			} else {
				return false;
			}
		}
		if (this.getValue(type) >= value) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean isEnoughTotal(CurrencyType type, long value) {
		if (this.getValueTotal(type) >= value) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public long getValueTotal(CurrencyType... types) {
		long sum = 0;
		for (CurrencyType type : types) {
			if (!totalCurrencies.containsKey(type)) {
				totalCurrencies.put(type, 0l);
			}
			sum += this.totalCurrencies.get(type);
		}
		return sum;
	}

	@JsonIgnore
	public long getValue(CurrencyType... types) {
		long sum = 0;
		for (CurrencyType type : types) {
			if (!currencies.containsKey(type)) {
				currencies.put(type, 0l);
				totalCurrencies.put(type, 0l);
			}
			sum += this.currencies.get(type);
		}
		return sum;
	}

	public Map<CurrencyType, Long> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<CurrencyType, Long> currencies) {
		this.currencies = currencies;
	}

	@JsonIgnore
	public void collectUpdate(Map<Integer, Long> result, Map<Integer, Long> totalResult) {
		for (int i = 0, j = marks.length(); i < j; i++) {
			if (marks.get(i)) {
				CurrencyType type = CurrencyType.valueOf(i);
				result.put(i, currencies.get(type));
				totalResult.put(i, totalCurrencies.get(type));
			}
			marks.set(i, false);
		}
	}

	public Map<CurrencyType, Long> getTotalCurrencies() {
		return totalCurrencies;
	}

	public void setTotalCurrencies(Map<CurrencyType, Long> totalCurrencies) {
		this.totalCurrencies = totalCurrencies;
	}

}
