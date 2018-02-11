package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.QuestMonsterDieEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_QuestMonster_Spawn;
import com.windforce.common.event.core.EventBusManager;

@Component
public class QuestMonsterTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.QUEST_MONSTER;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		Quest quest = (Quest) contexts.get(TriggerContextKey.QUEST);
		String spawnGroupId = resource.getKeys().get(TriggerContextKey.SPAWNGROUPID);
		final Long owner = player.getObjectId();
		final String questId = quest.getId();
		final Npc npc = (Npc) SpawnManager.getInstance().spawnObject(spawnGroupId, player.getInstanceId());

		if (quest.getQuestMonsters() == null) {
			quest.setQuestMonsters(new CopyOnWriteArrayList<Npc>());
		}

		quest.getQuestMonsters().add(npc);

		broadcast(resource, npc, true);

		Future<?> future = null;
		if (resource.getKeys().containsKey(TriggerContextKey.DESPAWN)) {
			long delay = Long.valueOf(resource.getKeys().get(TriggerContextKey.DESPAWN));
			future = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!npc.isSpawned()) {
						World.getInstance().despawn(npc);
						broadcast(resource, npc, false);
					}
				}
			}, delay);
		}

		final Future<?> finalFuture = future;
		npc.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				if (player.getKnownList().knowns(npc)) {
					EventBusManager.getInstance().submit(
							QuestMonsterDieEvent.valueOf(owner, questId, npc.getSpawn().getKey()));
				}
				if (finalFuture != null && !finalFuture.isCancelled()) {
					finalFuture.cancel(false);
				}
				broadcast(resource, npc, false);
			}
		});
	}

	/**
	 * 通报地图上的人
	 * 
	 * @param resource
	 * @param npc
	 */
	private void broadcast(TriggerResource resource, Npc npc, boolean spawn) {
		if (resource.getKeys().containsKey(TriggerContextKey.BROADCAST_MAP)) {
			if (!resource.getKeys().get(TriggerContextKey.BROADCAST_MAP).equals("true")) {
				return;
			}
			// 通报地图上的人
			Iterator<Player> players = npc.getPosition().getMapRegion().getParent().playerIterator();
			while (players.hasNext()) {
				Player p = players.next();
				PacketSendUtility.sendPacket(p,
						SM_QuestMonster_Spawn.valueOf(npc.getObjectId(), npc.getObjectKey(), (byte) (spawn ? 1 : 0)));
			}
		}

	}
}
