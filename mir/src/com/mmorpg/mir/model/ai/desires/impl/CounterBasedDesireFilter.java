package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.desires.Desire;
import com.mmorpg.mir.model.ai.desires.DesireIteratorFilter;

public class CounterBasedDesireFilter implements DesireIteratorFilter {
	@Override
	public boolean isOk(Desire desire) {
		return desire.isReadyToRun();
	}
}
