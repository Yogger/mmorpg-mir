package com.mmorpg.mir.model.kingofwar.facade;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.KingOfWarInfo;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_CommandPlayerList;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_Enter;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_ForbidChat;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_Leave;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_Rank;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_Rank_All;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_SetKingCommand;
import com.mmorpg.mir.model.kingofwar.packet.CM_KingOfWar_Status;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_CommandPlayerList;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Rank;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Rank_All;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Status;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class KingOfWarFacade {
	private static Logger logger = Logger.getLogger(KingOfWarFacade.class);
	@Autowired
	private KingOfWarManager kingOfWarManager;

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@Autowired
	private KingOfWarConfig kingOfWarConfig;

	@PostConstruct
	public void init() {

		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				// 验证是否满足开启的条件
				long openTime = kingOfWarManager.getOpenServerNextKingOfWarTime();
				if (openTime == 0) {
					return;
				}
				long now = System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE * 5;
				if (openTime > now) {
					return;
				}
				if (DateUtils.isToday(new Date(openTime))) {
					startKingOfWar(false);
				} else {

					Date lastMergeTime = ServerState.getInstance().getServerEnt().getLastMergeTime();
					if (lastMergeTime != null
							&& DateUtils.calcIntervalDays(lastMergeTime, new Date()) < kingOfWarConfig.MERGE_DATE_NOT_OPEN
									.getValue()) {
						return;
					}

					if (lastMergeTime != null
							&& DateUtils.calcIntervalDays(lastMergeTime, new Date()) == kingOfWarConfig.MERGE_DATE_NOT_OPEN
									.getValue()) {
						startKingOfWar(true);
					}

					CoreConditions coreConditions = new CoreConditions();
					coreConditions.addConditions(CoreConditionType.createBetweenCronTimeCondition("0 0 0 * * SUN",
							"0 0 0 * * MON"));
					if (coreConditions.verify(null, false)) {
						startKingOfWar(false);
					}
				}

			}

			@Override
			public String getName() {
				return "咸阳争夺战开始前的通报计时器";
			}
		}, KingOfWarConfig.getInstance().START_TIME_NOTICE.getValue());
	}

	private void startKingOfWar(final boolean mergeFirst) {
		I18nUtils i18nUtils = I18nUtils.valueOf("601004");
		ChatManager.getInstance().sendSystem(61001, i18nUtils, null);

		// 5分钟后正式开启
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				kingOfWarManager.start(mergeFirst);
			}
		}, DateUtils.MILLIS_PER_MINUTE * 5);
	}

	public static void main(String[] args) {
		// long now = System.currentTimeMillis();
		// Calendar cal = Calendar.getInstance();
		// cal.setFirstDayOfWeek(Calendar.MONDAY);
		// for (int i = 0; i < 20; i++) {
		// long time = now + (i * DateUtils.MILLIS_PER_DAY);
		// cal.setTime(new Date(time));
		// int w = cal.get(Calendar.DAY_OF_WEEK);
		// System.out.println(w);
		// System.out.println("first:" + cal.getFirstDayOfWeek());
		// }
		// Date now = new Date();
		System.out.println(DateUtils.getNextTime("0 0 0 ? * MON", new Date()));
		System.out.println(DateUtils.getNextTime("0 0 0 * * MON", new Date()));
	}

	@HandlerAnno
	public void enterWar(TSession session, CM_KingOfWar_Enter req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			kingOfWarManager.enterWar(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void leaveWar(TSession session, CM_KingOfWar_Leave req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			kingOfWarManager.leaveWar(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getPointsRank(TSession session, CM_KingOfWar_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_KingOfWar_Rank sm = SM_KingOfWar_Rank.valueOf(kingOfWarManager.getTopPlayerWarInfo(player));
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getPointsRank(TSession session, CM_KingOfWar_Rank_All req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			int size = KingOfWarConfig.getInstance().POINTS_RANK_SIZE.getValue();
			int start = (req.getPage() - 1) * size;
			int end = req.getPage() * size;
			SM_KingOfWar_Rank_All sm = SM_KingOfWar_Rank_All.valueOf(kingOfWarManager.getTopPlayerWarInfo(start, end),
					(kingOfWarManager.getRankTemp().size() / size) + 1);
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getCommandPlayerVO(TSession session, CM_KingOfWar_CommandPlayerList req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_KingOfWar_CommandPlayerList sm = SM_KingOfWar_CommandPlayerList.valueOf(kingOfWarManager
					.getCommandPlayerVOs(player, req.getCountryId()));
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void setKingCommand(TSession session, CM_KingOfWar_SetKingCommand req) {
		if (req.getCommand() != null && req.getCommand().length() > 20) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			kingOfWarManager.setKingCommand(player, req.getCommand());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getCurrentStatus(TSession session, CM_KingOfWar_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_KingOfWar_Status sm = kingOfWarManager.getKingOfWarStatus();
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void forbidChat(TSession session, CM_KingOfWar_ForbidChat req) {
		if (!KingOfWarConfig.getInstance().KINGOFKING_CAN_FORBID.getValue()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			kingOfWarManager.forbidChat(player, target, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("禁言", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (player.isKingOfking()) {
			I18nUtils i18nUtils = I18nUtils.valueOf("301001");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(0, i18nUtils, null);
			player.getGameStats().addModifiers(KingOfWarInfo.KINGOFKING_STATE_ID,
					KingOfWarConfig.getInstance().KINGOFKING_STATS.getValue());
		}
	}

	@ReceiverAnno
	public void logout(LogoutEvent event) {
		if (kingOfWarManager.getPlayerWarInfos().containsKey(event.getOwner())) {
			kingOfWarManager.sendPlayerCount();
		}
	}

}
