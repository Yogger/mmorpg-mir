package com.mmorpg.mir.model.fashion.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.FashionConfig;
import com.mmorpg.mir.model.fashion.manager.FashionManager;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.fashion.packet.CM_Fashion_Operate_Hide;
import com.mmorpg.mir.model.fashion.packet.CM_Fashion_UnWear;
import com.mmorpg.mir.model.fashion.packet.CM_Fashion_Upgrade;
import com.mmorpg.mir.model.fashion.packet.CM_Fashion_Wear;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.event.BecomKingEvent;
import com.mmorpg.mir.model.gangofwar.event.KingAbdicateEvent;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.kingofwar.event.BecomeKingOfKingEvent;
import com.mmorpg.mir.model.kingofwar.event.KingOfKingAbdicateEvent;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class FashionFacade {
	private static Logger logger = Logger.getLogger(FashionFacade.class);
	@Autowired
	private FashionManager fashionManager;

	@HandlerAnno
	public void wearFashion(TSession session, CM_Fashion_Wear req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			fashionManager.wearFashion(player, req.getFashionId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("穿戴时装出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void unWearFashion(TSession session, CM_Fashion_UnWear req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			fashionManager.unWearFashion(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("脱下时装出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void upgrade(TSession session, CM_Fashion_Upgrade req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			fashionManager.upgrade(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("升级时装出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void operateHide(TSession session, CM_Fashion_Operate_Hide req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			fashionManager.operateHide(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("隐藏时装出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void becomeKingOfKing(BecomeKingOfKingEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			return;
		}
		int kingOfKingFashionId = FashionConfig.getInstance().KING_OF_KING_FASHIONID.getValue();
		FashionPool pool = player.getFashionPool();
		pool.gainFashion(kingOfKingFashionId);
		pool.wear(kingOfKingFashionId);
	}

	@ReceiverAnno
	public void kingOfkingAbdicate(KingOfKingAbdicateEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			return;
		}
		int kingOfKingFashionId = FashionConfig.getInstance().KING_OF_KING_FASHIONID.getValue();
		FashionPool pool = player.getFashionPool();
		pool.removeFashion(kingOfKingFashionId);
		PlayerManager.getInstance().updateIfOffline(player);
	}

	@ReceiverAnno
	public void becomKingOfKing(BecomKingEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			return;
		}
		int kingFashionId = FashionConfig.getInstance().KING_FASHIONID.getValue();
		FashionPool pool = player.getFashionPool();
		pool.gainFashion(kingFashionId);
		pool.wear(kingFashionId);
	}

	@ReceiverAnno
	public void kingAbdicate(KingAbdicateEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			return;
		}
		int kingFashionId = FashionConfig.getInstance().KING_FASHIONID.getValue();
		FashionPool pool = player.getFashionPool();
		pool.removeFashion(kingFashionId);
		PlayerManager.getInstance().updateIfOffline(player);
	}

	@ReceiverAnno
	public void loginCheckKing(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());

		FashionPool pool = player.getFashionPool();
		int kingFashionId = FashionConfig.getInstance().KING_FASHIONID.getValue();
		if (player.isKing()) {
			if (!pool.containFashion(kingFashionId)) {
				pool.gainFashion(kingFashionId);
				I18nUtils title = I18nUtils.valueOf(FashionConfig.getInstance().LOGIN_KING_MAIL_TITLE.getValue());
				I18nUtils content = I18nUtils.valueOf(FashionConfig.getInstance().LOGIN_KING_MAIL_CONTENT.getValue());

				Mail mail = Mail.valueOf(title, content, null, null);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}
		}
		boolean containKingFashion = pool.containFashion(kingFashionId);
		if (!player.isKing() && containKingFashion) {
			pool.removeFashion(kingFashionId);
		}

		int kingOfKingFashionId = FashionConfig.getInstance().KING_OF_KING_FASHIONID.getValue();
		if (player.isKingOfking()) {
			if (!pool.containFashion(kingOfKingFashionId)) {
				pool.gainFashion(kingOfKingFashionId);
				I18nUtils title = I18nUtils.valueOf(FashionConfig.getInstance().LOGIN_KING_OF_KING_MAIL_TITLE
						.getValue());
				I18nUtils content = I18nUtils.valueOf(FashionConfig.getInstance().LOGIN_KING_OF_KING_MAIL_CONTENT
						.getValue());

				Mail mail = Mail.valueOf(title, content, null, null);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}
		}
		boolean containKOKFashionId = pool.containFashion(kingOfKingFashionId);
		if (!player.isKingOfking() && containKOKFashionId) {
			pool.removeFashion(kingOfKingFashionId);
		}

		if (pool.getCurrentFashionId() != -1) {
			int currentFashionId = pool.getCurrentFashionId();
			if (!pool.containFashion(currentFashionId)) {
				pool.setCurrentFashionId(-1);
			}
		}

	}
}
