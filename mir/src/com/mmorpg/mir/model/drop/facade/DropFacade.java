package com.mmorpg.mir.model.drop.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.drop.packet.SM_Boss_First_Blood;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class DropFacade {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private BossManager bossManager;

	@ReceiverAnno
	public void bossDropEvent(BossDieEvent event) {
		BossResource res = bossManager.getBossResource(event.getBossId(), true);
		if (event.isKnowPlayer() && !res.isElite()) {
			Player player = playerManager.getPlayer(event.getOwner());
			if (!player.getDropHistory().getHuntBossHistory().containsKey(event.getBossId())) {
				BossResource resource = bossManager.getBossResource(event.getBossId(), true);
				if (resource.getFBRewardConditions().verify(player, false)
						&& resource.getRewardChooserGroup() != null) {
					if (!player.getDropHistory().getBossFHRewardHistory().containsKey(event.getBossId())) {
						PacketSendUtility.sendPacket(player, SM_Boss_First_Blood.valueOf(event.getBossId()));
						player.getDropHistory().getBossFHRewardHistory().put(event.getBossId(), false);
					}
					player.getDropHistory().getHuntBossHistory().put(event.getBossId(), System.currentTimeMillis());
				}
			}
		}
	}

	@ReceiverAnno
	public void clearDropCount(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getDropHistory().refresh();
	}

}
