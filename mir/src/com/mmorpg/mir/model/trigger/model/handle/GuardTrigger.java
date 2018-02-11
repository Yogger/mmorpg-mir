package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.LorryDieEvent;
import com.mmorpg.mir.model.gameobjects.event.LorryRouteOverEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.event.core.EventBusManager;

@Component
public class GuardTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.GUARD;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		Quest quest = (Quest) contexts.get(TriggerContextKey.QUEST);
		String spawnGroupId = resource.getKeys().get(TriggerContextKey.SPAWNGROUPID);
		final Long owner = player.getObjectId();
		final String questId = quest.getId();
		final Lorry lorry = (Lorry) SpawnManager.getInstance().spawnObject(spawnGroupId, player.getInstanceId());
		lorry.getObserveController().addObserver(new ActionObserver(ObserverType.ROUTEOVER) {
			@Override
			public void die(Creature creature) {
				EventBusManager.getInstance().submit(LorryDieEvent.valueOf(owner, questId));
			}

			@Override
			public void routeOver() {
				EventBusManager.getInstance().submit(LorryRouteOverEvent.valueOf(owner, questId));
				lorry.getController().delete();
			}
		});
	}

}
