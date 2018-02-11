package com.mmorpg.mir.model.transport.manager;

import java.util.Map;

import com.mmorpg.mir.model.chat.model.show.object.FlyPositionShow;
import com.mmorpg.mir.model.transport.model.PlayerChatTransport;

public interface IPlayerChatTransportManager {
	public FlyPositionShow addTransport(int mapId, int x, int y, int instanceId);

	PlayerChatTransport getChatTransport(int id);

	Map<Integer, PlayerChatTransport> getPlayerTransports();
}
