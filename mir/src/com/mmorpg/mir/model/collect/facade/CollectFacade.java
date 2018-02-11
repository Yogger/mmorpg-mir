package com.mmorpg.mir.model.collect.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.collect.manager.CollectManager;
import com.mmorpg.mir.model.collect.packet.CM_Reward_Collect;
import com.mmorpg.mir.model.collect.packet.CM_Reward_FamedGeneral;
import com.mmorpg.mir.model.collect.packet.SM_Reward_FamedGeneral;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.event.CollectItemsEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CollectFacade {
	
	private static final Logger logger = Logger.getLogger(CollectFacade.class);
	@Autowired
	private PlayerManager playerManager;
	
	@Autowired
	private CollectManager collectManager;
	
	@HandlerAnno
	public void rewardCollect(TSession session, CM_Reward_Collect req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			collectManager.rewardCollect(player, req.getCollectId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家领取灵魂装备收集奖励", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public SM_Reward_FamedGeneral activeCollectGeneral(TSession session, CM_Reward_FamedGeneral req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Reward_FamedGeneral sm = new SM_Reward_FamedGeneral();
		try {
			sm = collectManager.activeCollectGeneral(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家领取名将武魂收集奖励", e);
			PacketSendUtility.sendErrorMessage(player);
		}
		return sm;
	}
	
	@ReceiverAnno
	public void doCollectItems(CollectItemsEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		collectManager.doCollectEquipment(player, event.getItems());
	}
}
