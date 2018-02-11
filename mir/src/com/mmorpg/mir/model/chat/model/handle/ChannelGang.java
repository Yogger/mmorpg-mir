package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.model.GangPosition;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelGang extends ChannelHandle {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private GangManager gangManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		if (args != null && args.length != 0 && args[0] instanceof Gang) {
			Gang gang = (Gang) args[0];
			gang.send(message, GangPosition.Member, SessionManager.getInstance());
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
			if (!player.isInGang()) {
				return;
			}
			gangManager.sendMessage(message, player.getPlayerGang().getGangId(), player.getObjectId());
		}

	}

	@Override
	public ChannelType getType() {
		return ChannelType.GANG;
	}

}
