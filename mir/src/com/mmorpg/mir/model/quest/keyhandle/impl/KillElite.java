package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.HashMap;
import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class KillElite extends QuestKeyHandle {

	public static final String LEVELMIN = "LEVELMIN";
	public static final String LEVELMAX = "LEVELMAX";
	public static final String MAPID = "MAPID";

	@Override
	public KeyType getType() {
		return KeyType.ELITE;
	}

	// 所接收的事件
	@ReceiverAnno
	public void addMonsterKill(BossDieEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				boolean success = false;
				if (key.getType() == this.getType()) {
					if (key.getParms() != null) {
						BossResource resource = BossManager.getInstance().getBossResource(event.getBossId(), true);
						if (!resource.isElite()) {
							continue;
						}
						SpawnGroupResource spawnRes = SpawnManager.getInstance().getSpawn(event.getSpawnKey());
						ObjectResource objRes = ObjectManager.getInstance().getObjectResource(spawnRes.getObjectKey());
						Integer mapId = (Integer) key.getParms().get(MAPID);
						Integer minLevel = (Integer) key.getParms().get(LEVELMIN);
						Integer maxLevel = (Integer) key.getParms().get(LEVELMAX);
						boolean checkLevelMin = (minLevel != null ? objRes.getLevel() >= minLevel : true);
						boolean checkLevelMax = (minLevel != null ? objRes.getLevel() <= maxLevel : true);
						boolean checkMapId = (mapId != null ? spawnRes.getMapId() == mapId : true);
						success = (checkLevelMin && checkLevelMax && checkMapId);
					} else if (key.getParms() == null || key.getParms().isEmpty()) {
						success = true;
					}
				}
				if (success) {
					key.addValue();
					needUpdate.add(quest);
				}
			}
		}
		player.getQuestPool().refreshQuest(needUpdate);
	}

	@Override
	public QuestKey create(QuestKeyResource resource, long now, Player player) {
		QuestKey key = new QuestKey();
		key.setType(this.getType());
		key.setValue(0);
		if (resource.getKeyname() == null) {
			key.setKeyname(resource.getType().name());
		} else {
			key.setKeyname(resource.getKeyname());
		}
		if (resource.getParms() != null) {
			key.setParms(new HashMap<String, Object>());
			key.getParms().put(LEVELMIN, resource.getParms().get(LEVELMIN));
			key.getParms().put(LEVELMAX, resource.getParms().get(LEVELMAX));
			key.getParms().put(MAPID, resource.getParms().get(MAPID));
		}
		return key;
	}

}
