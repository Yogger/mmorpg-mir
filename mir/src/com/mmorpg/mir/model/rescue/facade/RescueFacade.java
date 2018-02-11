package com.mmorpg.mir.model.rescue.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.GatherEvent;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rescue.packet.CM_Rescue_Complete;
import com.mmorpg.mir.model.rescue.packet.CM_Rescue_Start;
import com.mmorpg.mir.model.rescue.packet.CM_Rescue_Use_Item;
import com.mmorpg.mir.model.rescue.packet.SM_Rescue_AnotherDay;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.mmorpg.mir.model.welfare.event.RescueClawbackEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class RescueFacade {
	private static Logger logger = Logger.getLogger(RescueFacade.class);

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void complete(TSession session, CM_Rescue_Complete req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getRescue().chatReward();
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void start(TSession session, CM_Rescue_Start req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getRescue().initStart();
			PacketSendUtility.sendPacket(player, player.getRescue());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void useRescueItem(TSession session, CM_Rescue_Use_Item req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getRescue().useRescueItem();
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("使用营救令出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void anotherDay(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().refresh();
		if (player.getRescue().getItems().isEmpty()) {
			player.getRescue().resetAllItem(player.getCountryValue());
		}
		PacketSendUtility.sendPacket(player, SM_Rescue_AnotherDay.valueOf(player.getRescue()));
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().refresh();
		if (player.getRescue().getItems().isEmpty()) {
			player.getRescue().resetAllItem(player.getCountryValue());
		}
	}

	@ReceiverAnno
	public void vipChange(VipEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().refresh();
		if (player.getRescue().getItems().isEmpty()) {
			player.getRescue().resetAllItem(player.getCountryValue());
		}
	}

	@ReceiverAnno
	public void militaryEvent(MilitaryRankUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getRescue().getItems().isEmpty()) {
			if (player.getRescue().resetAllItem(player.getCountryValue())) {
				PacketSendUtility.sendPacket(player, player.getRescue());
			}
		} else {
			player.getRescue().accpetLastRescueItem();
		}
	}

	@ReceiverAnno
	public void rescueClawback(RescueClawbackEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().refresh();
		if (player.getRescue().getItems().isEmpty()) {
			player.getRescue().resetAllItem(player.getCountryValue());
		}
	}

	@ReceiverAnno
	public void gatherEvent(GatherEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().gatherEvent(event);
	}

	@ReceiverAnno
	public void monterEvent(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		player.getRescue().monsterEvent(event);
	}
}
