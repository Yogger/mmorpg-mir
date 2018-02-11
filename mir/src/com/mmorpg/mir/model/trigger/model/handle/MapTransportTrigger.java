package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.world.World;

@Component
public class MapTransportTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.TANSPORT;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		int mapId = Integer.valueOf(resource.getKeys().get(TriggerContextKey.MAP_ID));
		int x = Integer.valueOf(resource.getKeys().get(TriggerContextKey.X));
		int y = Integer.valueOf(resource.getKeys().get(TriggerContextKey.Y));
		World.getInstance().setPosition(player, mapId, x, y, (byte) 0);
		player.sendUpdatePosition();
		World.getInstance().spawn(player);
	}
}
