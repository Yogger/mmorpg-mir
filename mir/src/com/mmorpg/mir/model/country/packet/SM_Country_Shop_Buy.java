package com.mmorpg.mir.model.country.packet;

import java.util.Map;

public class SM_Country_Shop_Buy {
	private Map<String, Integer> buyed;

	public static SM_Country_Shop_Buy valueOf(Map<String, Integer> buyed) {
		SM_Country_Shop_Buy sm = new SM_Country_Shop_Buy();
		sm.buyed = buyed;
		return sm;
	}

	public Map<String, Integer> getBuyed() {
		return buyed;
	}

	public void setBuyed(Map<String, Integer> buyed) {
		this.buyed = buyed;
	}

}
