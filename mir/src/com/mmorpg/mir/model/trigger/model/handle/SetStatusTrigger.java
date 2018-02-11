package com.mmorpg.mir.model.trigger.model.handle;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class SetStatusTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.SET_STATUSNPC;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		StatusNpc npc = (StatusNpc) contexts.get(TriggerContextKey.STATUS_NPC);
		String status = resource.getKeys().get(TriggerContextKey.STATUS_NPC_VALUE);
		if (status == null) {
			throw new RuntimeException("SET_STATUSNPC npc status null");
		}
		if (npc == null) {
			String npcId = (String) resource.getKeys().get(TriggerContextKey.STATUS_NPC_ID);
			if (npcId == null) {
				throw new RuntimeException("SET_STATUSNPC npc null");
			}
			WorldMapInstance worldMapInstance = null;
			if (contexts.containsKey(TriggerContextKey.MAP_INSTANCE)) {
				worldMapInstance = (WorldMapInstance) contexts.get(TriggerContextKey.MAP_INSTANCE);
			} else {
				Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
				worldMapInstance = player.getPosition().getMapRegion().getParent();
			}
			List<StatusNpc> npcs = worldMapInstance.findObjectByType(ObjectType.STATUS_NPC);
			for (StatusNpc n : npcs) {
				if (n.getObjectKey().equals(npcId)) {
					n.setStatus(Integer.valueOf(status));
				}
			}

		} else {
			npc.setStatus(Integer.valueOf(status));
		}

	}

}
