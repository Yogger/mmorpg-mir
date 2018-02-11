package com.mmorpg.mir.model.clientdb.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.clientdb.packet.CM_CLIENT_SETTINGS;
import com.mmorpg.mir.model.clientdb.packet.CM_KEYBOARD;
import com.mmorpg.mir.model.clientdb.packet.SM_CLIENT_SETTINGS;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.service.PlayerService;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class KeyBoardFacade {

	private static final Logger logger = Logger.getLogger(KeyBoardFacade.class);
	@Autowired
	private PlayerService playerService;

	@HandlerAnno
	public void save(TSession session, CM_KEYBOARD keyboard) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (keyboard.getKeys().length() > 1000) {
				return;
			}
			playerService.setKeyboard(player, keyboard.getKeys());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("保存客户端快捷键", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}

	}

	@HandlerAnno
	public void saveClientSettings(TSession session, CM_CLIENT_SETTINGS req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (req.getClientSettings().length() > 1000) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.CLIENT_SETTINGS_TOO_LONG);
				return;
			}
			playerService.setClientSettings(player, req.getClientSettings());
			PacketSendUtility.sendPacket(player, SM_CLIENT_SETTINGS.valueOf());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("保存客户端设置", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

}
