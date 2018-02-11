package com.mmorpg.mir.model.mail.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mail.entity.MailEnt;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.packet.CM_DeleteMail;
import com.mmorpg.mir.model.mail.packet.CM_ReadMail;
import com.mmorpg.mir.model.mail.packet.CM_RewardMail;
import com.mmorpg.mir.model.mail.packet.SM_DeleteMail;
import com.mmorpg.mir.model.mail.packet.SM_ReadMail;
import com.mmorpg.mir.model.mail.packet.SM_RewardMail;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class MailFacade {

	private static final Logger logger = Logger.getLogger(MailFacade.class);

	@Autowired
	private MailManager mailManger;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public SM_ReadMail readMail(TSession session, CM_ReadMail req) {
		SM_ReadMail sm = new SM_ReadMail();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			mailManger.readMail(player, req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家读邮件异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_DeleteMail deleteMail(TSession session, CM_DeleteMail req) {
		SM_DeleteMail sm = new SM_DeleteMail();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			mailManger.deleteMail(player, req.getIndexs(), req.isDirect());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家删除邮件异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_RewardMail rewardMail(TSession session, CM_RewardMail req) {
		SM_RewardMail sm = new SM_RewardMail();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			mailManger.rewardMail(player, req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家领取邮件奖励异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@ReceiverAnno
	public void playerLogin(LoginEvent event) {
		try {
			MailEnt mailEnt = mailManger.getMailEnt(event.getOwner());
			if (mailEnt.getMailBox().getPlayerId() == null) {
				Player player = playerManager.getPlayer(event.getOwner());
				mailEnt.getMailBox().setPlayerId(player.getObjectId());
				mailEnt.getMailBox().setAccountName(player.getPlayerEnt().getAccountName());
				mailEnt.getMailBox().setName(player.getPlayerEnt().getName());
			}
			mailManger.receiveGroupMail(event.getOwner());
		} catch (Exception e) {
			logger.error("玩家领取邮件奖励异常", e);
		}
	}
}
