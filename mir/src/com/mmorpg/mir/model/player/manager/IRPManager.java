package com.mmorpg.mir.model.player.manager;

import com.mmorpg.mir.model.player.resource.RPResource;
import com.mmorpg.mir.model.player.resource.RPType;

public interface IRPManager {
	public void init();

	public RPResource getResource(int resourceId);

	public RPResource getResource(RPType type);

	public int getRPMaxLimit();

	public int getDailyReduce(int rpId);

	public int getNotBelow(int rpId);

	public int getKilledCount();

	public int getKilledPeriod();

	public int getGraySkillId(int index);

	public int getGraySkillSize();

	public RPType getRpType(int rpValue);
}
