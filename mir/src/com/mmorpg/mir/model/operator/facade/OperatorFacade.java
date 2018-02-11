package com.mmorpg.mir.model.operator.facade;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.operator.model.GmPrivilegeType;
import com.mmorpg.mir.model.operator.packet.CM_360Gift_Reward;
import com.mmorpg.mir.model.operator.packet.CM_Browser_2345_Reward;
import com.mmorpg.mir.model.operator.packet.CM_Draw_QiHu360_Privilege_Reward;
import com.mmorpg.mir.model.operator.packet.CM_Draw_QiHu360_Speed_Privilege_Reward;
import com.mmorpg.mir.model.operator.packet.CM_MobilePhone_Reward;
import com.mmorpg.mir.model.operator.packet.CM_OpVip_Reward;
import com.mmorpg.mir.model.operator.packet.CM_OpVip_Reward2;
import com.mmorpg.mir.model.operator.packet.CM_Operator_Ban;
import com.mmorpg.mir.model.operator.packet.CM_Operator_ForbidChat;
import com.mmorpg.mir.model.operator.packet.CM_Operator_Hide;
import com.mmorpg.mir.model.operator.packet.CM_Operator_Kick;
import com.mmorpg.mir.model.operator.packet.CM_Operator_ShowGMNickname;
import com.mmorpg.mir.model.operator.packet.CM_Operator_Trace;
import com.mmorpg.mir.model.operator.packet.CM_Operator_UnBan;
import com.mmorpg.mir.model.operator.packet.CM_Operator_UnForbidChat;
import com.mmorpg.mir.model.operator.packet.CM_Operator_UnHide;
import com.mmorpg.mir.model.operator.packet.CM_Operator_UnShowGMNickname;
import com.mmorpg.mir.model.operator.packet.CM_QiHu_Speed_BallReward_Reward;
import com.mmorpg.mir.model.operator.packet.CM_Set_OperatorClientInfo;
import com.mmorpg.mir.model.operator.packet.CM_WeChat_Reward;
import com.mmorpg.mir.model.operator.packet.SM_360Gift_Reward;
import com.mmorpg.mir.model.operator.packet.SM_Browser_2345_Reward;
import com.mmorpg.mir.model.operator.packet.SM_MobilePhone_Reward;
import com.mmorpg.mir.model.operator.packet.SM_OpVip_Reward;
import com.mmorpg.mir.model.operator.packet.SM_OpVip_Reward2;
import com.mmorpg.mir.model.operator.packet.SM_Operator_Ban;
import com.mmorpg.mir.model.operator.packet.SM_Operator_ForbidChat;
import com.mmorpg.mir.model.operator.packet.SM_Operator_Hide;
import com.mmorpg.mir.model.operator.packet.SM_Operator_Kick;
import com.mmorpg.mir.model.operator.packet.SM_Operator_ShowGMNickname;
import com.mmorpg.mir.model.operator.packet.SM_Operator_Trace;
import com.mmorpg.mir.model.operator.packet.SM_Operator_UnBan;
import com.mmorpg.mir.model.operator.packet.SM_Operator_UnForbidChat;
import com.mmorpg.mir.model.operator.packet.SM_Operator_UnHide;
import com.mmorpg.mir.model.operator.packet.SM_Operator_UnShowGMNickname;
import com.mmorpg.mir.model.operator.packet.SM_QiHu360_Privilege_Reward;
import com.mmorpg.mir.model.operator.packet.SM_QiHu360_Speed_Privilege_Reward;
import com.mmorpg.mir.model.operator.packet.SM_QiHu_Speed_Ball_Reward;
import com.mmorpg.mir.model.operator.packet.SM_Set_OperatorClientInfo;
import com.mmorpg.mir.model.operator.packet.SM_WeChat_Reward;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.CreateEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class OperatorFacade {
	private static Logger logger = Logger.getLogger(OperatorFacade.class);
	@Autowired
	private OperatorManager operatorManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public SM_MobilePhone_Reward rewardMoble(TSession session, CM_MobilePhone_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.rewardMobileReward(player);
		} catch (ManagedException e) {
			return SM_MobilePhone_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_MobilePhone_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_MobilePhone_Reward.valueOf(0);
	}

	@HandlerAnno
	public SM_QiHu_Speed_Ball_Reward qihuSpeedBallReward(TSession session, CM_QiHu_Speed_BallReward_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.qihuSpeedBallReward(player);
		} catch (ManagedException e) {
			return SM_QiHu_Speed_Ball_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("领取微信礼包异常", e);
			return SM_QiHu_Speed_Ball_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_QiHu_Speed_Ball_Reward.valueOf(0);
	}

	@HandlerAnno
	public SM_WeChat_Reward wechatReward(TSession session, CM_WeChat_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.wechatReward(player);
		} catch (ManagedException e) {
			return SM_WeChat_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_WeChat_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_WeChat_Reward.valueOf(0);
	}

	@HandlerAnno
	public SM_Browser_2345_Reward browser2345Reward(TSession session, CM_Browser_2345_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.browser2345Reward(player);
		} catch (ManagedException e) {
			return SM_Browser_2345_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("领取2345浏览器礼包异常", e);
			return SM_Browser_2345_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Browser_2345_Reward.valueOf(0);
	}

	@HandlerAnno
	public SM_OpVip_Reward rewardOpVip(TSession session, CM_OpVip_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.rewardOperatorVipReward(player, req.getLevel());
		} catch (ManagedException e) {
			return SM_OpVip_Reward.valueOf(req.getLevel(), e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_OpVip_Reward.valueOf(req.getLevel(), ManagedErrorCode.SYS_ERROR);
		}
		return SM_OpVip_Reward.valueOf(req.getLevel(), 0);
	}

	@HandlerAnno
	public SM_OpVip_Reward2 rewardOpVip(TSession session, CM_OpVip_Reward2 req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.rewardOperatorVipReward2(player, req.getId());
		} catch (ManagedException e) {
			return SM_OpVip_Reward2.valueOf(req.getId(), e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_OpVip_Reward2.valueOf(req.getId(), ManagedErrorCode.SYS_ERROR);
		}
		return SM_OpVip_Reward2.valueOf(req.getId(), 0);
	}

	@HandlerAnno
	public SM_Operator_Ban ban(TSession session, CM_Operator_Ban req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.ban(player, req.getName());
		} catch (ManagedException e) {
			return SM_Operator_Ban.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_Ban.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_Ban.valueOf(0);
	}

	@HandlerAnno
	public SM_360Gift_Reward ban(TSession session, CM_360Gift_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.reward360gift(player);
		} catch (ManagedException e) {
			return SM_360Gift_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_360Gift_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_360Gift_Reward.valueOf(0);
	}

	@HandlerAnno
	public SM_Set_OperatorClientInfo setOperatorClientInfo(TSession session, CM_Set_OperatorClientInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		player.getOperatorPool().setOperatorClientInfo(req.getOperatorClientInfo());
		return SM_Set_OperatorClientInfo.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_UnBan unBan(TSession session, CM_Operator_UnBan req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.unban(player, req.getName());
		} catch (ManagedException e) {
			return SM_Operator_UnBan.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_UnBan.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_UnBan.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_ShowGMNickname showGMNickname(TSession session, CM_Operator_ShowGMNickname req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.showGMNickname(player);
		} catch (ManagedException e) {
			return SM_Operator_ShowGMNickname.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_ShowGMNickname.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_ShowGMNickname.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_UnShowGMNickname unShowGMNickname(TSession session, CM_Operator_UnShowGMNickname req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.unShowGMNickname(player);
		} catch (ManagedException e) {
			return SM_Operator_UnShowGMNickname.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_UnShowGMNickname.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_UnShowGMNickname.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_Hide hide(TSession session, CM_Operator_Hide req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.hide(player);
		} catch (ManagedException e) {
			return SM_Operator_Hide.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_Hide.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_Hide.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_UnHide unHide(TSession session, CM_Operator_UnHide req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.unHide(player);
		} catch (ManagedException e) {
			return SM_Operator_UnHide.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_UnHide.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_UnHide.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_ForbidChat forbidChat(TSession session, CM_Operator_ForbidChat req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.forbidChat(player, req.getName(), req.getMinuteTime());
		} catch (ManagedException e) {
			return SM_Operator_ForbidChat.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_ForbidChat.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_ForbidChat.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_UnForbidChat removeChat(TSession session, CM_Operator_UnForbidChat req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.removeChat(player, req.getName());
		} catch (ManagedException e) {
			return SM_Operator_UnForbidChat.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_UnForbidChat.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_UnForbidChat.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_Kick kick(TSession session, CM_Operator_Kick req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.kick(player, req.getName());
		} catch (ManagedException e) {
			return SM_Operator_Kick.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_Kick.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_Operator_Kick.valueOf(0);
	}

	@HandlerAnno
	public SM_Operator_Trace trace(TSession session, CM_Operator_Trace req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			return operatorManager.trace(player, req.getName());
		} catch (ManagedException e) {
			return SM_Operator_Trace.valueOf(null, e.getCode());
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			return SM_Operator_Trace.valueOf(null, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_QiHu360_Privilege_Reward drawQiHu360PrivilegeReward(TSession session, CM_Draw_QiHu360_Privilege_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_QiHu360_Privilege_Reward res = null;
		try {
			res = operatorManager.rewardQiHu360Privilege(player, req.getId());
		} catch (ManagedException e) {
			return SM_QiHu360_Privilege_Reward.valueOf(e.getCode(), req.getId());
		} catch (Exception e) {
			logger.error("发送领取错误消息", e);
			return SM_QiHu360_Privilege_Reward.valueOf(ManagedErrorCode.SYS_ERROR, req.getId());
		}
		return res;
	}

	@HandlerAnno
	public SM_QiHu360_Speed_Privilege_Reward drawQiHu360SpeedPrivilegeReward(TSession session,
			CM_Draw_QiHu360_Speed_Privilege_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			operatorManager.rewardQiHu360SpeedPrivilege(player, req.getId());
		} catch (ManagedException e) {
			return SM_QiHu360_Speed_Privilege_Reward.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error("发送领取错误消息", e);
			return SM_QiHu360_Speed_Privilege_Reward.valueOf(ManagedErrorCode.SYS_ERROR);
		}
		return SM_QiHu360_Speed_Privilege_Reward.valueOf(0);
	}

	@ReceiverAnno
	public void login(LoginEvent loginEvent) {
		Player player = playerManager.getPlayer(loginEvent.getOwner());
		if (player.getOperatorPool().getGmPrivilege().havePrivilege(GmPrivilegeType.HIDE)) {
			operatorManager.hide(player);
		}

		if (player.getOperatorPool().getGmPrivilege().havePrivilege(GmPrivilegeType.NICKNAME)) {
			player.getEffectController().setAbnormal(EffectId.GM_NICKNAME, true);
		}
		if (!DateUtils.isSameDay(new Date(), new Date(player.getOperatorPool().getOperatorVip().getLastClearTime()))) {
			player.getOperatorPool().getOperatorVip().dailyClear();
		}
	}

	@ReceiverAnno
	public void anotherday(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getOperatorPool().getOperatorVip().dailyClear();
	}

	@ReceiverAnno
	public void create(CreateEvent createEvent) {
		Player player = playerManager.getPlayer(createEvent.getOwner());
		operatorManager.createReward(player);
	}
}
