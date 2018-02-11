package com.mmorpg.mir.model.transport.manager;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.show.object.FlyPositionShow;
import com.mmorpg.mir.model.transport.model.PlayerChatTransport;
import com.windforce.common.utility.New;

@Component
public class PlayerChatTransportManager implements IPlayerChatTransportManager {
	private AtomicInteger idCreator = new AtomicInteger();
	private Map<Integer, PlayerChatTransport> playerTransports = New.concurrentHashMap();

	private static PlayerChatTransportManager INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static PlayerChatTransportManager getInstance() {
		return INSTANCE;
	}

	synchronized public FlyPositionShow addTransport(int mapId, int x, int y, int instanceId) {
		PlayerChatTransport playerTransport = PlayerChatTransport.valueOf(idCreator.getAndIncrement(), mapId, x, y,
				instanceId);
		playerTransports.put(playerTransport.getId(), playerTransport);
		if (playerTransports.size() >= 1000) {
			clearTransports();
		}
		return FlyPositionShow.valueOf(playerTransport);
	}

	private void clearTransports() {
		int min = Integer.MAX_VALUE;
		for (PlayerChatTransport pct : playerTransports.values()) {
			if (pct.getId() <= min) {
				min = pct.getId();
			}
		}
		for (int i = min; i < min + 100; i++) {
			playerTransports.remove(i);
		}
	}

	public PlayerChatTransport getChatTransport(int id) {
		return getPlayerTransports().get(id);
	}

	public Map<Integer, PlayerChatTransport> getPlayerTransports() {
		return playerTransports;
	}

}
