package com.mmorpg.mir.model.promote.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.manager.PromotionManager;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class PromotionFacade {
	
	@Autowired
	private PromotionManager promotionManager;

	@ReceiverAnno
	public void promote(QuestCompletEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		promotionManager.selfPromote(player, event.getQuestId());
	}
	
	@ReceiverAnno
	public void open(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (ModuleOpenManager.getInstance().isOpenByKey(player, "opmk55")) {
			promotionManager.openPromote(player);
		}
	}
	
}
