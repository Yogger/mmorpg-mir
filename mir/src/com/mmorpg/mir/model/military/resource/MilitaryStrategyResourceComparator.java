package com.mmorpg.mir.model.military.resource;

import java.util.Comparator;

public class MilitaryStrategyResourceComparator implements Comparator<MilitaryStrategyResource> {

	@Override
	public int compare(MilitaryStrategyResource o1, MilitaryStrategyResource o2) {
		return o1.getId() - o2.getId();
	}

}