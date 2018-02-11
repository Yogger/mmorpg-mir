package com.mmorpg.mir.model.chat.model.handle;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelLocalLine extends ChannelHandle {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		int mapId = 0;
		if (args != null && args.length != 0 && args[0] instanceof Integer) {
			mapId = (Integer) args[0];
		}
		if (sender == null && mapId == 0) {
			throw new RuntimeException(String.format("message sender is null json[%s]",
					JsonUtils.object2String(message)));
		}
		if (sender != null && sender.getId() != 0) {
			Player player = playerManager.getPlayer(sender.getId());
			if (!player.getPosition().isSpawned()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_NOT_IN_MAP);
			}
			mapId = player.getPosition().getMapId();
		}
		if (mapId != 0) {
			for (WorldMapInstance worldMapInstance : World.getInstance().getWorldMap(mapId).getInstances().values()) {
				if (worldMapInstance.getInstanceId() == (Integer) args[1]) {
					Iterator<Player> players = worldMapInstance.playerIterator();
					while (players.hasNext()) {
						Player player = players.next();
						PacketSendUtility.sendPacket(player, message);
					}
				}
			}
		}

	}

	@Override
	public ChannelType getType() {
		return ChannelType.LOCAL_LINE;
	}

}
