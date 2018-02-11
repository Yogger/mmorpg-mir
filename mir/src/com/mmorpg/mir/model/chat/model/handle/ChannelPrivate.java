package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelPrivate extends ChannelHandle {

	public static final long SYSTEM_SENDER = -1L;
	@Autowired
	private PlayerManager playerManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		Sender sender = message.getSender();
		if (args != null && args.length == 1 && args[0] instanceof Long) {
			sender = new Sender();
			sender.setId((Long) args[0]);
		}
		if (args != null && args.length == 2 && args[1] instanceof Long) {
			message.setReciver((Long) args[1]);
		}
		// if (sender == null) {
		// throw new
		// RuntimeException(String.format("message sender is null json[%s]",
		// JsonUtils.object2String(message)));
		// }
		Player player = null;
		if (sender != null) {
			player = playerManager.getPlayer(sender.getId());
			if (player == null) {
				throw new RuntimeException(String.format("message player is null json[%s]",
						JsonUtils.object2String(message)));
			}
		}
		if (message.getReciver() == 0) {
			throw new RuntimeException(String.format("message reciverId is null json[%s]",
					JsonUtils.object2String(message)));
		}
		if (player != null) {
			if (ChatManager.getInstance().isInBlackList(player.getObjectId(), message.getReciver())) {
				PacketSendUtility.sendPacket(player, message);
				return;
			}
		}

		if (!SessionManager.getInstance().isOnline(message.getReciver()) && sender != null) {
			throw new ManagedException(ManagedErrorCode.PLAYER_INLINE);
		}

		Player targetPlayer = playerManager.getPlayer(message.getReciver());
		PacketSendUtility.sendPacket(targetPlayer, message);
		if (player != null) {
			PacketSendUtility.sendPacket(player, message);
		}

	}

	@Override
	public ChannelType getType() {
		return ChannelType.PRIVATE;
	}

}
