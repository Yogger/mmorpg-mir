package com.mmorpg.mir.model.quest.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.resource.QuestResource;


public interface IQuestManager {
	public void init();

	public void registerActivityHandle(QuestKeyHandle questKeyHandle);

	public Quest createQuest(QuestResource resource, Player player, long now);

	public short[] convertTemplateId(List<String> ids);

	public short convertTemplateId(String id);

	public HashMap<Short, Integer> convertTemplateId(Map<String, Integer> ids);

	public void doLoginRefresh(Player player);

	public void doLoginRefresh(Player player, boolean send);

	public void loginRefresh(LoginEvent event);

	public Collection<QuestResource> getAllResources();
}
