package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class ChannelAll extends ChannelHandle {
	
	private static final int CHAT_WORLD_CHANNEL = 5;

	@Autowired
	private SessionManager sessionManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		boolean useBlackList = sender != null && sessionManager.isOnline(sender.getId()) && message.getChannel() == CHAT_WORLD_CHANNEL;
		if (resource.getFilterConditions() != null) {
			for (Long playerId : sessionManager.getOnlineIdentities()) {
				Player player = PlayerManager.getInstance().getPlayer(playerId);
				if (resource.getCoreFilterConditions().verify(player, false)) {
					PacketSendUtility.sendPacket(player, message);
				}
			}
		} else {
			for (Long playerId : sessionManager.getOnlineIdentities()) {
				if (useBlackList && ChatManager.getInstance().isInBlackList(sender.getId(), playerId)) {
					continue;
				}
				Player player = PlayerManager.getInstance().getPlayer(playerId);
				PacketSendUtility.sendPacket(player, message);
			}
		}
	}

	@Override
	public ChannelType getType() {
		return ChannelType.ALL;
	}

}
