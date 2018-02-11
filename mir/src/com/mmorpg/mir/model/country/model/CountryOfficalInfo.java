package com.mmorpg.mir.model.country.model;

public class CountryOfficalInfo {
	private CountryOfficial officalEnum;
	private int index;

	public static CountryOfficalInfo valueOf(CountryOfficial officalEnum, int index) {
		CountryOfficalInfo info = new CountryOfficalInfo();
		info.officalEnum = officalEnum;
		info.index = index;
		return info;
	}

	public CountryOfficial getOfficalEnum() {
		return officalEnum;
	}

	public void setOfficalEnum(CountryOfficial officalEnum) {
		this.officalEnum = officalEnum;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
