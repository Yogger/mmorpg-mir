package com.mmorpg.mir.model.footprint.facade;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.footprint.model.FootprintPool;
import com.mmorpg.mir.model.footprint.packet.CM_Close_Footprint;
import com.mmorpg.mir.model.footprint.packet.CM_Open_Footprint;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class FootprintFacade {
	private static Logger logger = Logger.getLogger(FootprintFacade.class);
	@HandlerAnno
	public void open(TSession session, CM_Open_Footprint req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getFootprintPool().open(req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("开启足迹", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void close(TSession session, CM_Close_Footprint req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getFootprintPool().close(FootprintPool.CLOSE);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("关闭足迹", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
}
