package com.mmorpg.mir.model.achievement.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.achievement.manager.AchievementManager;
import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.event.event.IEvent;

public class AchievementFacade {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private AchievementManager achievementManager;
	
	@Autowired
	private ObjectManager objectManager;

	private void refershEventCondition(IEvent event, Player player) {
		for (Integer id : achievementManager.getAchievementIds(event)) {
			if (!player.getAchievement().isCompleted(id)) {
				achievementManager.tryComplete(id, player);
			}
		}
	}

	/** 所有事件都要在这里注册 */

	/**
	 * 等级事件
	 * 
	 * @param levelUpEvent
	 */
	@ReceiverAnno
	public void levelCondition(LevelUpEvent levelUpEvent) {
		Player player = playerManager.getPlayer(levelUpEvent.getOwner());
		refershEventCondition(levelUpEvent, player);
	}

	@ReceiverAnno
	public void addMonsterCount(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		ObjectResource objRes = objectManager.getObjectResource(event.getKey());
		if (objRes.getObjectType() == ObjectType.MONSTER) {
			player.getDropHistory().addHuntMonsterCount();
		}
	}
	
	@ReceiverAnno
	public void addBossCount(BossDieEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		BossResource res = BossManager.getInstance().getBossResource(event.getBossId(), true);
		if (!res.isElite()) {
			String objKey = SpawnManager.getInstance().getSpawn(event.getSpawnKey()).getObjectKey();
			player.getDropHistory().addHuntBossCount(objKey);
		} else {
			player.getDropHistory().addHuntMonsterCount();
		}
	}
	
	@ReceiverAnno
	public void addKillPlayer(KillPlayerEvent event) {
		if (!event.isKillOtherCountryPlayer()) { // 排除内奸
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		Player target = playerManager.getPlayer(event.getKilledPlayerId());
		if (target.isKing()) {
			player.getRp().addKillKingCount();
		}
		player.getRp().addKillEnemyCount();
	}
}
