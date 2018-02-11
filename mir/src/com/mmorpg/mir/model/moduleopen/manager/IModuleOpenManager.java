package com.mmorpg.mir.model.moduleopen.manager;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.resource.ModuleOpenResource;

public interface IModuleOpenManager {
	boolean isOpenByModuleKey(Player player, ModuleKey moduleKey);

	boolean isOpenByKey(Player player, String key);

	void refreshAll(Player player);

	void completeQuestOpenModule(Player player, String questId);

	ModuleOpenResource getResource(String id);
}
