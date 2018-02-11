package com.mmorpg.mir.model.soul.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.resource.ModuleOpenResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.soul.core.SoulService;
import com.mmorpg.mir.model.soul.packet.CM_Soul_Uplevel;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Uplevel;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class SoulFacade {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SoulFacade.class);

	@Autowired
	private SoulService soulService;

	/**
	 * 英魂进阶
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@HandlerAnno
	public SM_Soul_Uplevel uplevel(TSession session, CM_Soul_Uplevel request) {
		SM_Soul_Uplevel sm = new SM_Soul_Uplevel();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			player.getSoul().refreshBlessing(player);
			sm = soulService.uplevel(player, request.getUseCurrency() == 1);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			LOGGER.error("英魂进阶 | " + e.getMessage(), e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	/**
	 * 完成指定任务, 开启英魂系统
	 * 
	 * @param event
	 */
	@ReceiverAnno
	public void openSoul(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ModuleOpenResource resource = ModuleOpenManager.getInstance().getResource(event.getModuleResourceId());
		if (resource.getModuleKey() == ModuleKey.SOUL_PF.value()) {
			soulService.flushSoul(player, true, true);
		}
	}

}
