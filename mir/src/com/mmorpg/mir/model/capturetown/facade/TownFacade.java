package com.mmorpg.mir.model.capturetown.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.capturetown.manager.TownManager;
import com.mmorpg.mir.model.capturetown.packet.CM_Apply_Capture_Town;
import com.mmorpg.mir.model.capturetown.packet.CM_Buy_Motion;
import com.mmorpg.mir.model.capturetown.packet.CM_ChallengeTown;
import com.mmorpg.mir.model.capturetown.packet.CM_Clear_CD;
import com.mmorpg.mir.model.capturetown.packet.CM_Get_Country_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.CM_Get_My_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.CM_Get_Specified_Town_Info;
import com.mmorpg.mir.model.capturetown.packet.CM_Get_Towns_Info;
import com.mmorpg.mir.model.capturetown.packet.CM_Leave_ChallengeTown;
import com.mmorpg.mir.model.capturetown.packet.CM_Recieved_Town_Reward;
import com.mmorpg.mir.model.capturetown.packet.CM_Refresh_Town;
import com.mmorpg.mir.model.capturetown.packet.SM_Apply_Capture_Town;
import com.mmorpg.mir.model.capturetown.packet.SM_Buy_Motion;
import com.mmorpg.mir.model.capturetown.packet.SM_ChallengeTown;
import com.mmorpg.mir.model.capturetown.packet.SM_Clear_CD;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_Country_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_My_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Leave_ChallengeTown;
import com.mmorpg.mir.model.capturetown.packet.SM_Recieved_Town_Reward;
import com.mmorpg.mir.model.capturetown.packet.SM_Refresh_Town;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.scheduler.Scheduled;
import com.windforce.common.scheduler.ValueType;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class TownFacade {

	private static final Logger logger = Logger.getLogger(TownFacade.class);

	@Autowired
	private TownManager townManager;

	@Scheduled(name = "定时刷新城池的数据和玩家的数据", value = "@townConfig.resetAndRewardTime", type = ValueType.SPEL)
	public void rewardAndReset() {
		if (ClearAndMigrate.clear) {
			return;
		}
		townManager.rewardAndReset();
	}

	@HandlerAnno
	public void getTownsInfo(TSession session, CM_Get_Towns_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			townManager.getTownsInfo(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获得所有的城池信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getSpecifiedTownInfo(TSession session, CM_Get_Specified_Town_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			townManager.getTargetTownInfo(player, req.getKey());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取对应城池的信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public SM_ChallengeTown challengeTown(TSession session, CM_ChallengeTown req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_ChallengeTown sm = new SM_ChallengeTown();
		try {
			sm.setNextChanllengeTime(townManager.challengeTownOwner(player, req.getKey()));
			sm.setType(player.getPlayerCountryHistory().getCaptureTownInfo().canEnter() ? 0 : 1);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("挑战城池", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Refresh_Town refreshTownStats(TSession session, CM_Refresh_Town req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Refresh_Town sm = new SM_Refresh_Town();
		try {
			sm.setTownInfos(townManager.getTownCountryInfo());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("刷新所有城池的数据", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Leave_ChallengeTown leaveTownCopyMap(TSession session, CM_Leave_ChallengeTown req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Leave_ChallengeTown sm = new SM_Leave_ChallengeTown();
		try {
			townManager.leaveTownCopy(player, true);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("离开挑战的副本", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Apply_Capture_Town applyCaptureTown(TSession session, CM_Apply_Capture_Town req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Apply_Capture_Town sm = new SM_Apply_Capture_Town();
		try {
			townManager.applyCaptureTown(player, req.getKey());
			sm.setKey(req.getKey());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("申请入驻空城", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Recieved_Town_Reward recievedTownReward(TSession session, CM_Recieved_Town_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Recieved_Town_Reward sm = new SM_Recieved_Town_Reward();
		try {
			sm.setFeats(townManager.recievedTownReward(player));
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("领取战争储备", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Buy_Motion buyPlayerMotion(TSession session, CM_Buy_Motion req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Buy_Motion sm = new SM_Buy_Motion();
		try {
			sm = townManager.buyMotion(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("购买行动力", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Clear_CD clearCD(TSession session, CM_Clear_CD req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Clear_CD sm = new SM_Clear_CD();
		try {
			townManager.clearCDByGold(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("重置CD时间", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Get_My_Fight_Info getMyTownFightInfo(TSession session, CM_Get_My_Fight_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Get_My_Fight_Info sm = new SM_Get_My_Fight_Info();
		try {
			sm = townManager.getMyFightInfo(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("获取个人战报", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@HandlerAnno
	public SM_Get_Country_Fight_Info getCountryFightInfo(TSession session, CM_Get_Country_Fight_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Get_Country_Fight_Info sm = new SM_Get_Country_Fight_Info();
		try {
			sm = townManager.getCountryFightLog(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("获取国家战报", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

	@ReceiverAnno
	public void openTownModule(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		townManager.occupyPveTown(player);
	}

	@ReceiverAnno
	public void loginStartBaseFeatsTask(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getPlayerCountryHistory().getCaptureTownInfo().startAutoRewardTask(true);
	}

	@ReceiverAnno
	public void refreshPlayerDaily(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getPlayerCountryHistory().getCaptureTownInfo().refreshDaily();
	}

	@ReceiverAnno
	public void logoutLeaveTownCopyMapAndStopTask(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		try {
			townManager.leaveTownCopy(player, false);
			player.getPlayerCountryHistory().getCaptureTownInfo().stopAutoRewardTask();
		} catch (Exception e) {
			logger.error("离开副本断线异常", e);
		}
	}

}
