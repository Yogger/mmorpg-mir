package com.mmorpg.mir.model.relive.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface PlayerReliveService {
	void respawn(Player player, boolean isUseItem, boolean reliveType, boolean notLogin);
}
