package com.mmorpg.mir.model.monsterriot.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.monsterriot.manager.MonsterRiotManager;
import com.mmorpg.mir.model.monsterriot.packet.CM_Query_MonsterRiot;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class MonsterRiotFacade {
	
	private static final Logger logger = Logger.getLogger(MonsterRiotFacade.class);

	@Autowired
	private MonsterRiotManager manager;
	
	/*@ReceiverAnno
	public void monsterKill(MonsterKillEvent monsterKillEvent) {
		Player player = playerManager.getPlayer(monsterKillEvent.getOwner());
		if (manager.isInAct(player.getCountryId())) {
			manager.addCount(player, monsterKillEvent.getSpawnKey());
		}
	}*/
	
	@HandlerAnno
	public void queryMapMonsterStatus(TSession session, CM_Query_MonsterRiot req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			manager.queryMapMonsterStatus(player, req.getMapId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查询地图怪物情况失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
}
