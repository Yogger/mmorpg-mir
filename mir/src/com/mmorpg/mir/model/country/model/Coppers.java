package com.mmorpg.mir.model.country.model;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.country.model.vo.CoppersVO;

/**
 * 国库
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-16
 * 
 */
public class Coppers {
	private Map<CoppersType, Long> countryCoppers;

	public Map<CoppersType, Long> getCountryCoppers() {
		return countryCoppers;
	}

	public void setCountryCoppers(Map<CoppersType, Long> countryCoppers) {
		this.countryCoppers = countryCoppers;
	}

	@JsonIgnore
	private BitSet marks = new BitSet(CoppersType.values().length);

	public static Coppers valueOf() {
		Coppers purse = new Coppers();
		purse.countryCoppers = New.hashMap();
		return purse;
	}

	public CoppersVO creatCoppersVO() {
		CoppersVO vo = new CoppersVO();
		vo.setCurrencies(new HashMap<String, Long>());
		for (Entry<CoppersType, Long> entry : countryCoppers.entrySet()) {
			vo.getCurrencies().put(entry.getKey().name(), entry.getValue());
		}
		return vo;
	}

	@JsonIgnore
	public long cost(CoppersType type, long value) {
		long remain = this.getValue(type);
		if (value <= remain) {
			countryCoppers.put(type, remain - value);
			marks.set(type.getValue());
			return 0;
		} else {
			// 不足
			countryCoppers.put(type, 0l);
			marks.set(type.getValue());
			return value - remain;
		}
	}

	@JsonIgnore
	public void add(CoppersType type, Long value) {
		countryCoppers.put(type, this.getValue(type) + value);
		marks.set(type.getValue());
	}

	@JsonIgnore
	public boolean isEnough(CoppersType type, Long value) {
		if (this.getValue(type) >= value) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public long getValue(CoppersType... types) {
		long sum = 0;
		for (CoppersType type : types) {
			if (!countryCoppers.containsKey(type)) {
				countryCoppers.put(type, 0l);
			}
			sum += this.countryCoppers.get(type);
		}
		return sum;
	}

	@JsonIgnore
	public Map<Integer, Long> collectUpdate() {
		Map<Integer, Long> result = New.hashMap();
		for (int i = 0, j = marks.length(); i < j; i++) {
			if (marks.get(i)) {
				result.put(i, countryCoppers.get(CoppersType.valueOf(i)));
			}
			marks.set(i, false);
		}
		return result;
	}

}
