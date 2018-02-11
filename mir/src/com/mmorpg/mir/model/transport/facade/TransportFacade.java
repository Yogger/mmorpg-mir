package com.mmorpg.mir.model.transport.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.transport.packet.CM_PlayerChatTransport;
import com.mmorpg.mir.model.transport.packet.CM_PlayerTransport;
import com.mmorpg.mir.model.transport.packet.CM_Transport;
import com.mmorpg.mir.model.transport.packet.SM_PlayerTransport;
import com.mmorpg.mir.model.transport.packet.SM_Transport;
import com.mmorpg.mir.model.transport.service.TransportService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class TransportFacade {
	private static final Logger logger = Logger.getLogger(TransportFacade.class);

	@Autowired
	public TransportService transportService;

	@HandlerAnno
	public SM_Transport transport(TSession session, CM_Transport cm_Transport) {
		SM_Transport sm = new SM_Transport();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = transportService.transport(cm_Transport.getId(), player);
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家传送处理异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_PlayerTransport transport(TSession session, CM_PlayerTransport cm) {
		SM_PlayerTransport sm = new SM_PlayerTransport();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = transportService.playerTransport(cm.getId(), player);
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家传送处理异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void chatTransport(TSession session, CM_PlayerChatTransport cm) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			transportService.playerChatTransport(cm.getId(), player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
}
