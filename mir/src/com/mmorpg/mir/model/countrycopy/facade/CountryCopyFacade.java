package com.mmorpg.mir.model.countrycopy.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.event.CountryTechnologyUpgradeEvent;
import com.mmorpg.mir.model.country.event.ReserveKingAbdicateEvent;
import com.mmorpg.mir.model.country.event.ReserveKingRetireEvent;
import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.countrycopy.manager.CountryCopyManager;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_Encourage;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_Enroll;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_Enter;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_Quit;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_Status;
import com.mmorpg.mir.model.countrycopy.packet.CM_CountryCopy_UnEnroll;
import com.mmorpg.mir.model.countrycopy.packet.CM_Encourage_Apply;
import com.mmorpg.mir.model.countrycopy.packet.CM_Get_Encourage_Info;
import com.mmorpg.mir.model.countrycopy.packet.CM_Get_Encourage_List;
import com.mmorpg.mir.model.countrycopy.packet.CM_TechCopy_Enter;
import com.mmorpg.mir.model.countrycopy.packet.CM_Tech_Copy_Quit;
import com.mmorpg.mir.model.countrycopy.packet.CM_Tech_Copy_Ranks;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CountryCopyFacade {
	
	private static final Logger logger = Logger.getLogger(CountryCopyFacade.class);
	
	@Autowired
	private CountryCopyManager countryCopyManager;
	
	@HandlerAnno
	public void countryCopyEnroll(TSession session, CM_CountryCopy_Enroll req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.enrolle(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("报名国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void countryCopyUnEnroll(TSession session, CM_CountryCopy_UnEnroll req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.unEnrolle(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("取消报名国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void enterCountryCopy(TSession session, CM_CountryCopy_Enter req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.enter(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void getEncourageList(TSession session, CM_Get_Encourage_List req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.getEncourageNameList(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void leaveCountryCopy(TSession session, CM_CountryCopy_Quit req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.quit(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("退出国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void applyEncourage(TSession session, CM_Encourage_Apply req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.applyEncourage(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("助威申请国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void encourage(TSession session, CM_CountryCopy_Encourage req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.encourage(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("退出国家副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void getEncourageInfo(TSession session, CM_Get_Encourage_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.getEncourageInfo(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国家副本助威信息", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void getCopyStatus(TSession session, CM_CountryCopy_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.getCountryCopyStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国家副本助威信息", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void enterCountryTechCopy(TSession session, CM_TechCopy_Enter req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.enterCountryTechCopy(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入墨家傀儡副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void leaveCountryTechCopy(TSession session, CM_Tech_Copy_Quit req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.leaveCountryTechCopy(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入墨家傀儡副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void getCountryTechCopyRanks(TSession session, CM_Tech_Copy_Ranks req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryCopyManager.getCountryTechCopyRanks(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入墨家傀儡副本失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@ReceiverAnno
	public void reserveKingAbdicate(ReserveKingAbdicateEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (player.getCountryCopyInfo().getEnterCount() >= CountryCopyConfig.getInstance().getDailyMaxenterCount()) {
			countryCopyManager.unEnrolleBySystem(player);
			player.getCountryCopyInfo().setEnterCount(CountryCopyConfig.getInstance().getDailyMaxenterCount());
		}
	}
	
	@ReceiverAnno
	public void reserveKingAbdicate(ReserveKingRetireEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (player.getCountryCopyInfo().getEnterCount() >= CountryCopyConfig.getInstance().getDailyMaxenterCount()) {
			countryCopyManager.unEnrolleBySystem(player);
			player.getCountryCopyInfo().setEnterCount(CountryCopyConfig.getInstance().getDailyMaxenterCount());
		}
	}
	
	@ReceiverAnno
	public void logOutUnEnroll(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		countryCopyManager.unEnrolleBySystem(player);
		countryCopyManager.removeTechCopySpeicialBuff(player);
	}

	@ReceiverAnno
	public void enterOtherCopy(CopyEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		countryCopyManager.unEnrolleBySystem(player);
	}
	
	@ReceiverAnno
	public void anotherDay(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getCountryCopyInfo().refresh();
	}
	
	@ReceiverAnno
	public void startTechCopy(CountryTechnologyUpgradeEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		try {
			countryCopyManager.startCountryTechCopy(player.getCountryId());
		} catch (ManagedException e) {
			// between time cond expception
		}
	}
	
}
