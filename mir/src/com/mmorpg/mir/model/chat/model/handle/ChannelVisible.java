package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelVisible extends ChannelHandle {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		if (sender == null) {
			throw new RuntimeException(String.format("message sender is null json[%s]",
					JsonUtils.object2String(message)));
		}
		Player player = playerManager.getPlayer(sender.getId());
		if (player == null) {
			throw new RuntimeException(String.format("message player is null json[%s]",
					JsonUtils.object2String(message)));
		}
		PacketSendUtility.sendPacket(player, message);
		for (VisibleObject obj : player.getKnownList()) {
			if (obj instanceof Player) {
				if (ChatManager.getInstance().isInBlackList(player.getObjectId(), obj.getObjectId()))
					continue;
				PacketSendUtility.sendPacket(((Player) obj), message);
			}
		}
	}

	@Override
	public ChannelType getType() {
		return ChannelType.CANSEE;
	}

}
