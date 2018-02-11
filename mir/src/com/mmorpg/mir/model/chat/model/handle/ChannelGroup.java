package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelGroup extends ChannelHandle {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		PlayerGroup group = null;
		if (args != null && args.length != 0 && args[0] instanceof PlayerGroup) {
			group = (PlayerGroup) args[0];
			group.send(PlayerGroup.SYSTEM_SENDER_ID, message);
		} else {
			if (sender == null) {
				throw new RuntimeException(String.format("message sender is null json[%s]",
						JsonUtils.object2String(message)));
			}
			Player player = playerManager.getPlayer(sender.getId());
			if (player == null) {
				throw new RuntimeException(String.format("message player is null json[%s]",
						JsonUtils.object2String(message)));
			}
			if (!player.isInGroup()) {
				return;
			}
			group = player.getPlayerGroup();
			group.send(sender.getId(), message);
		}

	}

	@Override
	public ChannelType getType() {
		return ChannelType.GROUP;
	}

}
