package com.mmorpg.mir.model.vip.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.operator.model.SuperVip;
import com.mmorpg.mir.model.operator.model.SuperVipVO;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.manager.VipManager;
import com.mmorpg.mir.model.vip.packet.CM_Vip_LevelReward;
import com.mmorpg.mir.model.vip.packet.CM_Vip_ReceiveTempVip;
import com.mmorpg.mir.model.vip.packet.CM_Vip_WeekReward;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class VipFacade {
	private static Logger logger = Logger.getLogger(VipFacade.class);

	@Autowired
	private VipManager vipManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void reciveWeekReward(TSession session, CM_Vip_WeekReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			vipManager.rewardWeekReward(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("VIP领取周奖励", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void reciveVipLevelReward(TSession session, CM_Vip_LevelReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			vipManager.rewardVipLevelReward(player, req.getLevel(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("VIP领取等级奖励", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void reciveTempVip(TSession session, CM_Vip_ReceiveTempVip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			vipManager.receiveTempVip(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("VIP领取等级奖励", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void loginout(LogoutEvent logoutEvent) {
		Player player = playerManager.getPlayer(logoutEvent.getOwner());
		player.getVip().addTempVip();
	}

	@Autowired
	private OperatorManager operatorManager;

	@ReceiverAnno
	public void superVip(RechargeRewardEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		SuperVip superVip = operatorManager.getSuperVip(player.getPlayerEnt().getServer());
		if (superVip != null && (superVip.isInit() || Debug.debug) && superVip.isOpen()) {
			if (player.getVip().isSuperVip(superVip)) {
				SuperVipVO vo = superVip.createVO(true);
				vo.setFirstChargeTime(player.getVip().firstChargeTime());
				vo.setTotalCharge(player.getVip().totalCharge());
				PacketSendUtility.sendPacket(player, vo);
			} else {
				SuperVipVO vo = superVip.createVO(false);
				vo.setFirstChargeTime(player.getVip().firstChargeTime());
				vo.setTotalCharge(player.getVip().totalCharge());
				PacketSendUtility.sendPacket(player, vo);
			}
		}
	}

}
