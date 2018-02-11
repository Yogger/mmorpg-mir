package com.mmorpg.mir.model.group.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.manager.GroupManager;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.group.packet.CM_APPLY_GROUP;
import com.mmorpg.mir.model.group.packet.CM_APPLY_LIST_PLAYER;
import com.mmorpg.mir.model.group.packet.CM_CHANGE_LEADER;
import com.mmorpg.mir.model.group.packet.CM_CREATE_GROUP;
import com.mmorpg.mir.model.group.packet.CM_DEAL_APPLY_GROUP;
import com.mmorpg.mir.model.group.packet.CM_EXPEL_GROUP;
import com.mmorpg.mir.model.group.packet.CM_GROUP_LIST;
import com.mmorpg.mir.model.group.packet.CM_INVITE_PLAYER;
import com.mmorpg.mir.model.group.packet.CM_QUIT_GROUP;
import com.mmorpg.mir.model.group.packet.SM_Member_Level_Change;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class GroupFacade {
	private static Logger logger = Logger.getLogger(GroupFacade.class);
	@Autowired
	private GroupManager groupManager;
	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void invite(TSession session, CM_INVITE_PLAYER req) {
		Player inviter = SessionUtil.getPlayerBySession(session);
		try {
			if (!SessionManager.getInstance().isOnline(req.getPlayerId())) {
				PacketSendUtility.sendErrorMessage(inviter, ManagedErrorCode.PLAYER_INLINE);
				return;
			}
			Player invited = playerManager.getPlayer(req.getPlayerId());
			groupManager.invitePlayerToGroup(inviter, invited);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(inviter, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("组队邀请出现未知异常", e);
			PacketSendUtility.sendPacket(inviter, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}

	}

	@HandlerAnno
	public void getAppList(TSession session, CM_APPLY_LIST_PLAYER req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			groupManager.getAppyList(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("组队获取邀请列表出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void apply(TSession session, CM_APPLY_GROUP req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Player leader = playerManager.getPlayer(req.getLeaderId());
			groupManager.apply(player, leader);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("组队申请列表出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void dealApply(TSession session, CM_DEAL_APPLY_GROUP req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			groupManager.dealApply(player, req.getPlayerId(), req.getOk() == 0 ? false : true);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("组队处理申请出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getGroupList(TSession session, CM_GROUP_LIST req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			groupManager.getPlayerGroupList(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取组队列表出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void exple(TSession session, CM_EXPEL_GROUP req) {
		Player leader = SessionUtil.getPlayerBySession(session);
		try {
			Player target = playerManager.getPlayer(req.getPlayerId());
			groupManager.exple(leader, target);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("队长踢人出现未知异常", e);
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void createGroup(TSession session, CM_CREATE_GROUP req) {
		Player leader = SessionUtil.getPlayerBySession(session);
		try {
			groupManager.createPlayerGroup(leader);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("创建队伍出现未知异常", e);
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void changeLeader(TSession session, CM_CHANGE_LEADER req) {
		Player leader = SessionUtil.getPlayerBySession(session);
		try {
			Player target = playerManager.getPlayer(req.getTarget());
			groupManager.changeLeader(leader, target);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("更换队长出现未知异常", e);
			PacketSendUtility.sendPacket(leader, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void quit(TSession session, CM_QUIT_GROUP req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			groupManager.removePlayerFromGroup(player, false);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("退出队伍出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void levelUpEvent(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (groupManager.isGroupMember(event.getOwner())) {
			PlayerGroup group = player.getPlayerGroup();
			group.refreshSimpleVO();
			SM_Member_Level_Change packet = SM_Member_Level_Change.valueOf(event.getOwner(), event.getLevel());
			for (Player p : group.getMembers()) {
				if (p.equals(player)) {
					continue;
				}
				if (SessionManager.getInstance().isOnline(p.getObjectId())) {
					PacketSendUtility.sendPacket(p, packet);
				}
			}
		}
	}

	@ReceiverAnno
	public void loginEvent(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		groupManager.onLogin(player);
	}

	@ReceiverAnno
	public void logoutEvent(LogoutEvent logoutEvent) {
		Player player = playerManager.getPlayer(logoutEvent.getOwner());
		if (player.isInGroup())
			groupManager.scheduleRemove(player);
	}
}
