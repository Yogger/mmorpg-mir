package com.mmorpg.mir.model.chat.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.packet.CM_Big_Face;
import com.mmorpg.mir.model.chat.packet.CM_Chat_Request;
import com.mmorpg.mir.model.chat.packet.CM_Query_ChatMan;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.world.packet.SM_PlayerChatSimple;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ChatFacade {

	private static Logger logger = Logger.getLogger(ChatFacade.class);
	@Autowired
	private ChatManager chatManager;

	@HandlerAnno
	public void send(TSession session, CM_Chat_Request req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			chatManager.send(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void sendEmotion(TSession session, CM_Big_Face req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			chatManager.sendEmotion(player, req.getFaceId(),false);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public SM_PlayerChatSimple queryCountryOnlineLocal(TSession session, CM_Query_ChatMan req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_PlayerChatSimple sm = new SM_PlayerChatSimple();
		try {
			sm = chatManager.queryCountryOnlineLocal(player, req.getPartName());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("查询私聊的玩家", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}
	
}
