package com.mmorpg.mir.model.boss.facade;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.model.BossDropInfo;
import com.mmorpg.mir.model.boss.model.BossView;
import com.mmorpg.mir.model.boss.packet.CM_Boss_BossView;
import com.mmorpg.mir.model.boss.packet.CM_Boss_Coins_Buy;
import com.mmorpg.mir.model.boss.packet.CM_Boss_Coins_Upgrade;
import com.mmorpg.mir.model.boss.packet.CM_Boss_DropInfo;
import com.mmorpg.mir.model.boss.packet.CM_Boss_Receive_FHReward;
import com.mmorpg.mir.model.boss.packet.CM_Boss_View;
import com.mmorpg.mir.model.boss.packet.SM_Boss_BossView;
import com.mmorpg.mir.model.boss.packet.SM_Boss_DropInfo;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class BossFacade {
	private static Logger logger = Logger.getLogger(BossFacade.class);
	@Autowired
	private BossManager bossManager;

	@HandlerAnno
	public void getAllBossStatus(TSession session, CM_Boss_BossView req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Map<String, BossView> bossViews = bossManager.getAllBossStatus(req.getGroup());
			PacketSendUtility.sendPacket(player, SM_Boss_BossView.valueOf(bossViews, req.getGroup()));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void getBossStatus(TSession session, CM_Boss_View req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, bossManager.getBossStatus(req.getId()));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void getAllBossDropInfo(TSession session, CM_Boss_DropInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ArrayList<BossDropInfo> drops = bossManager.getAllDropInfo();
			PacketSendUtility.sendPacket(player, SM_Boss_DropInfo.valueOf(drops));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void receiveBossFHReward(TSession session, CM_Boss_Receive_FHReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			bossManager.receiveBossFHReward(player, req.getBossId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void upgradeBossCoinsLevel(TSession session, CM_Boss_Coins_Upgrade req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			bossManager.upgradeBossCoinsLevel(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void buyBossCoinsExtraStats(TSession session, CM_Boss_Coins_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			bossManager.buyBossCoinsExtraStats(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void openBossCoinsModule(ModuleOpenEvent event) {
		bossManager.bosscoinModuleOpen(event);
	}
	
}
