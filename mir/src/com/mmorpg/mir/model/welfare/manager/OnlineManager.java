package com.mmorpg.mir.model.welfare.manager;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.resource.OnlineResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class OnlineManager implements IOnlineManager{

	private static OnlineManager instance;
	@Static
	public Storage<Integer, OnlineResource> onlineStorage;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static OnlineManager getInstance() {
		return instance;
	}

	public OnlineResource getOnlineResource(int index) {
		return onlineStorage.get(index, true);
	}

	public void startOnlineCount(Player player) {
		player.getWelfare().getOnlineReward().refreshTime(player);
	}
	
}
