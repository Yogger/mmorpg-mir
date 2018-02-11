package com.mmorpg.mir.model.welfare.manager;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public interface IPublicWelfareManager {
	int countLightNum(Player player);

	boolean tagLight(Player player, int tagId);

	Map<Integer, Boolean> getTagStatus(Player player);

	void reduceClawbackNum(Player player, int eventId, int num);
}
