package com.mmorpg.mir.model.chat.model.show;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ShowType;
import com.mmorpg.mir.model.chat.model.show.object.FlyPositionShow;
import com.mmorpg.mir.model.chat.model.show.object.PositionShow;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.transport.manager.PlayerChatTransportManager;

@Component
public class PositionShowHandle extends ShowHandle {

	@Override
	public ShowType getType() {
		return ShowType.POSITION;
	}

	@Autowired
	private PlayerChatTransportManager playerChatTransportManager;

	@Override
	public Object createShowObject(Player player, HashMap<String, String> requestShow) {
		if (!player.getPosition().isSpawned() || player.isInCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		FlyPositionShow show = playerChatTransportManager.addTransport(player.getMapId(), player.getX(), player.getY(),
				player.getInstanceId());
		PositionShow positionShow = new PositionShow();
		positionShow.setInstanceId(show.getInstanceId());
		positionShow.setMapId(show.getMapId());
		positionShow.setX(show.getX());
		positionShow.setY(show.getY());
		positionShow.setFlyId(show.getFlyId());
		return positionShow;
	}
}
