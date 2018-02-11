package com.mmorpg.mir.model.nickname.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.manager.NicknameManager;
import com.mmorpg.mir.model.nickname.packet.CM_Nickname_Active;
import com.mmorpg.mir.model.nickname.packet.CM_Nickname_Equip;
import com.mmorpg.mir.model.nickname.packet.CM_Nickname_UnEquip;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class NicknameFacade {
	private static Logger logger = Logger.getLogger(NicknameFacade.class);
	@Autowired
	private NicknameManager nicknameManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void activeNickName(TSession session, CM_Nickname_Active req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			nicknameManager.activeNickName(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("卸下称号异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void unEquip(TSession session, CM_Nickname_UnEquip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			nicknameManager.unEquip(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("卸下称号异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void equip(TSession session, CM_Nickname_Equip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			nicknameManager.equip(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("卸下称号异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		nicknameManager.refreshNickName(player, true);
	}
	
	@ReceiverAnno
	public void anotherDay(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		nicknameManager.refreshNickName(player, false);
	}
}
