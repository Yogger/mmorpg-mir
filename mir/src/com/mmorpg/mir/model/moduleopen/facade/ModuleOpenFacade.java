package com.mmorpg.mir.model.moduleopen.facade;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.event.CountryTechnologyUpgradeEvent;
import com.mmorpg.mir.model.countrycopy.event.CountryCopyFinishEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.packet.CM_Query_Module_Status;
import com.mmorpg.mir.model.moduleopen.packet.SM_Module_Open;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ModuleOpenFacade {

	private static final Logger logger = Logger.getLogger(ModuleOpenFacade.class);

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@HandlerAnno
	public void queryMyModuleOpenStatus(TSession session, CM_Query_Module_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_Module_Open sm = new SM_Module_Open();
			if (moduleOpenManager.isOpenByKey(player, req.getModuleId())) {
				ArrayList<String> list = New.arrayList(1);
				list.add(req.getModuleId());
				sm.setKeys(list);
				PacketSendUtility.sendPacket(player, sm);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查询玩家模块是否开启 ", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
		}
	}

	@ReceiverAnno
	public void levelUpEventReciever(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}

	@ReceiverAnno
	public void militaryRankUpgradeReciever(MilitaryRankUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}

	@ReceiverAnno
	public void questCompleteReciever(QuestCompletEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.completeQuestOpenModule(player, event.getQuestId());
	}

	@ReceiverAnno
	public void refreshVip(VipEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}

	@ReceiverAnno
	public void openFlagModule(CountryCopyFinishEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}

	@ReceiverAnno
	public void antherDay(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}

	@ReceiverAnno
	public void openNewTechModule(CountryTechnologyUpgradeEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		moduleOpenManager.refreshAll(player);
	}
}
