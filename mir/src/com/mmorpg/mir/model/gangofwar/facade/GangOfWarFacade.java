package com.mmorpg.mir.model.gangofwar.facade;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.gangofwar.packet.CM_GangOfWar_BackHome;
import com.mmorpg.mir.model.gangofwar.packet.CM_GangOfWar_Enter;
import com.mmorpg.mir.model.gangofwar.packet.CM_GangOfWar_Leave;
import com.mmorpg.mir.model.gangofwar.packet.CM_GangOfWar_Rank;
import com.mmorpg.mir.model.gangofwar.packet.CM_GangOfWar_Status;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_Rank;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_Status;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class GangOfWarFacade {
	private static Logger logger = Logger.getLogger(GangOfWarFacade.class);
	@Autowired
	private GangOfWarManager gangOfWarManager;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@HandlerAnno
	public void enterWar(TSession session, CM_GangOfWar_Enter req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangOfWarManager.enterWar(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void leaveWar(TSession session, CM_GangOfWar_Leave req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangOfWarManager.leaveWar(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void backhome(TSession session, CM_GangOfWar_BackHome req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangOfWarManager.backhome(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getPointsRank(TSession session, CM_GangOfWar_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			int size = GangOfWarConfig.getInstance().KILL_RANK_SIZE.getValue();
			int start = (req.getPage() - 1) * size;
			int end = req.getPage() * size;
			GangOfWar gow = gangOfWarManager.getGangOfwars().get(player.getCountryId());
			SM_GangOfWar_Rank sm = SM_GangOfWar_Rank.valueOf(gow.getTopPlayerWarInfo(start, end),
					(gow.notEmptySize() / size) + 1);
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getCurrentStatus(TSession session, CM_GangOfWar_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_GangOfWar_Status sm = gangOfWarManager.getGangOfwars().get(player.getCountryId()).getKingOfWarStatus();
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@PostConstruct
	public void gangOfWarStart() {
		simpleScheduler.schedule(new ScheduledTask() {
			@Override
			public void run() {
				gangOfWarManager.openWar(false);
			}

			@Override
			public String getName() {
				return String.format("家族战开始的时间");
			}

		}, GangOfWarConfig.getInstance().START_TIME_NOT_KING.getValue());

		// 判断是否发通知
		simpleScheduler.schedule(new ScheduledTask() {
			@Override
			public void run() {
				gangOfWarManager.openWar(true);
			}

			@Override
			public String getName() {
				return String.format("家族战开始的时间");
			}

		}, GangOfWarConfig.getInstance().NOTICE_START_TIME.getValue());
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.calcIntervalDays(new Date(), new Date()));
		// System.out.println(DateUtils.getNextTime("0 0 0 *** * ***　* *  * *",
		// new Date()));
	}

}
