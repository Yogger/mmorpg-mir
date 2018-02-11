package com.mmorpg.mir.model.trigger.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.trigger.packet.CM_Trigger_req;
import com.mmorpg.mir.model.trigger.service.TriggerService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class TriggerFacade {
	private static Logger logger = Logger.getLogger(TriggerFacade.class);

	@Autowired
	private TriggerService triggerService;

	@HandlerAnno
	public void send(TSession session, CM_Trigger_req req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			triggerService.clientTrigger(req.getId(), player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("客服端请求触发器异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
}
