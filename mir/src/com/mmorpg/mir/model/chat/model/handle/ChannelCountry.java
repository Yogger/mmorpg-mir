package com.mmorpg.mir.model.chat.model.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChannelCountry extends ChannelHandle {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public void send(ChannelResource resource, Message message, Object... args) {
		if (args != null && args.length != 0 && args[0] instanceof Country) {
			Country country = (Country) args[0];
			CoreConditions filterConditions = null;
			if (args.length == 2 && args[1] instanceof CoreConditions) {
				filterConditions = (CoreConditions) args[1];
				country.sendPackAll(message, filterConditions);
			} else {
				country.sendPackAll(message);
			}
		} else {
			Sender sender = message.getSender();
			if (sender != null) {
				Player player = playerManager.getPlayer(sender.getId());
				if (player == null) {
					throw new RuntimeException(String.format("message player is null json[%s]",
							JsonUtils.object2String(message)));
				}
				player.getCountry().sendPackAllByFilter(player.getObjectId(), message);
			}
		}

	}

	@Override
	public ChannelType getType() {
		return ChannelType.COUNTRY;
	}

}
