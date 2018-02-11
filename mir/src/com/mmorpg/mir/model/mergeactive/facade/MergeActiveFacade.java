package com.mmorpg.mir.model.mergeactive.facade;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.mergeactive.manager.MergeActiveManager;
import com.mmorpg.mir.model.mergeactive.packet.CM_Draw_Merge_Consume;
import com.mmorpg.mir.model.mergeactive.packet.CM_Get_Merge_Consume;
import com.mmorpg.mir.model.mergeactive.packet.CM_Merge_Cheap_Bag_Reward;
import com.mmorpg.mir.model.mergeactive.packet.CM_Merge_Login_Reward;
import com.mmorpg.mir.model.mergeactive.packet.SM_Get_Merge_Consume;
import com.mmorpg.mir.model.mergeactive.packet.SM_Merge_Consume_Push;
import com.mmorpg.mir.model.mergeactive.resource.MergeConsumeCompeteResource;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class MergeActiveFacade {
	private static final Logger logger = Logger.getLogger(MergeActiveFacade.class);
	
	@Autowired
	private MergeActiveManager mergeActiveManager;
	
	@Autowired
	private MergeActiveConfig mergeActiveConfig;
	
	@HandlerAnno
	public void drawMergeLoginGiftReward(TSession session, CM_Merge_Login_Reward req){
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			mergeActiveManager.drawMergeLoginGift(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取和服登陆有礼出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawMergeCheapGiftBagReward(TSession session, CM_Merge_Cheap_Bag_Reward req){
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			mergeActiveManager.drawMergeCheapGiftBag(player, req.getGiftId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("购买和服特惠礼包出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void drawMergeConsumeReward(TSession session, CM_Draw_Merge_Consume req){
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			mergeActiveManager.drawMergeConsumeReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("购买和服特惠礼包出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void getMergeConsume(TSession session, CM_Get_Merge_Consume req){
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, SM_Get_Merge_Consume.valueOf(player));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("购买和服特惠礼包出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@ReceiverAnno
	public void consumeGoldActiveEvent(CurrencyActionEvent event) {
		if (event.getType() == CurrencyType.GOLD) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			MergeConsumeCompeteResource resource = mergeActiveConfig.getConsumeActiveResource();
			if (resource.getLogDataConditions().verify(player)) {
				player.getMergeActive().getConsumeCompete().addConsumeGold(event.getValue());
			}

			if (resource.getEnterRankConditions().verify(player, false)) {
				event.setActivityValue(player.getMergeActive().getConsumeCompete().getConsumeGold());
				WorldRankManager.getInstance().submitRankRow(player, RankType.MERGE_ACTIVITY_CONSUME, event);
			}

			ArrayList<String> canRecieves = mergeActiveManager.getConsumeCanRecievesReward(player);
			if (!canRecieves.isEmpty()) {
				PacketSendUtility.sendPacket(player, SM_Merge_Consume_Push.valueOf(canRecieves));
			}
		}
	}
	
	@ReceiverAnno
	public void anotherday(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ArrayList<String> canRecieves = mergeActiveManager.getConsumeCanRecievesReward(player);
		if (!canRecieves.isEmpty()) {
			PacketSendUtility.sendPacket(player, SM_Merge_Consume_Push.valueOf(canRecieves));
		}
		mergeActiveManager.loginCompensateMail(player);
	}
	
	@ReceiverAnno
	public void compensateRewardByEmail(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		mergeActiveManager.loginCompensateMail(player);
	}
	
}
