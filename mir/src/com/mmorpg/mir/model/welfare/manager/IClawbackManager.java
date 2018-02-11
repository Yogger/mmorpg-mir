package com.mmorpg.mir.model.welfare.manager;

import java.util.List;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;

public interface IClawbackManager {
	ClawbackResource getClawbackResource(int eventId);

	boolean reduceCurrency(Player player, int currencyType, ClawbackResource resource, boolean throwException);

	void rewardBeatch(Player player, int clawbackType, int currencyType, List<Integer> eventIds);

	void reward(Player player, int clawbackType, int currencyType, ClawbackResource resource);

	void checkCondition(Player player);

	boolean isOpen(Player player, int eventId);

	boolean isClawbacked(Player player, int eventId);

	boolean pass(Player player, ClawbackResource resource);

	boolean canClawback(Player player, ClawbackResource resource);

	void checkTimeOut(Player player, int eventId);

	void exec(Player player, ClawbackEnum claw, int count);

	void exec(Player player, ClawbackEnum claw);
	
	public int getCorrectExeRuns(ClawbackResource resource, int level);
	
	public int getCorrectExeNums(ClawbackResource resource, int level);
}
