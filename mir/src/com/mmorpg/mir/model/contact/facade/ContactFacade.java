package com.mmorpg.mir.model.contact.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.contact.packet.CM_ADDTO_BLACKLIST;
import com.mmorpg.mir.model.contact.packet.CM_ADD_FRIEND;
import com.mmorpg.mir.model.contact.packet.CM_CANCEL_SHIELD;
import com.mmorpg.mir.model.contact.packet.CM_DELETE_ENEMY;
import com.mmorpg.mir.model.contact.packet.CM_DELETE_FRIEND;
import com.mmorpg.mir.model.contact.packet.CM_DELIVER_MOOD;
import com.mmorpg.mir.model.contact.packet.CM_GET_CONTACTINFO;
import com.mmorpg.mir.model.contact.packet.CM_QUERY_FRIENDS;
import com.mmorpg.mir.model.contact.packet.CM_SET_DISBAND_ADD;
import com.mmorpg.mir.model.contact.packet.CM_SET_MY_SETTINGS;
import com.mmorpg.mir.model.contact.service.ContactService;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.core.Wsession;

@Component
public class ContactFacade {

	private static final Logger logger = LoggerFactory.getLogger(ContactFacade.class);

	@Autowired
	private ContactService contactService;

	@Autowired
	private PlayerManager playerManager;

	public void getAllContactInformation(Wsession session, CM_GET_CONTACTINFO req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.getAllContactInformation(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void deliverMoodPhrase(Wsession session, CM_DELIVER_MOOD req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.deliverMoodPhrase(player, req.getPhrase(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void setMySettings(Wsession session, CM_SET_MY_SETTINGS req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.setMapPublic(player, req.isDisplayOffline(), req.isDisplayHead(), req.isPublicMapInfo(),
					req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void queryFriends(Wsession session, CM_QUERY_FRIENDS req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.queryFriends(player, req.getPartName());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void addAttentionFriend(Wsession session, CM_ADD_FRIEND req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.addAttentionFriend(player, req.getFriendId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void cancelAttentionFriend(Wsession session, CM_DELETE_FRIEND req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.cancelAttentionFriend(player, req.getTargetId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void loginSyncAndNotify(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		contactService.updateMySocialInfo(player, true);
		contactService.notifyMyFans(player);
	}

	@ReceiverAnno
	public void logoutNotify(LogoutEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		contactService.updateMySocialInfo(player, false);
		contactService.notifyMyFans(player);
	}

	@ReceiverAnno
	public void levelupSyncAndNotify(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		contactService.updateMySocialInfo(player, true);
		contactService.notifyMyFans(player);
	}

	@ReceiverAnno
	public void refreshPromotion(PromotionEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		contactService.updateMySocialInfo(player, true);
		contactService.notifyMyFans(player);
	}

	@ReceiverAnno
	public void addEnemy(PlayerDieEvent event) {
		if (event.isKilledByPlayer()) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			if (player.isInCountryActivityMap()) {
				return;
			}
			contactService.addEnemy(event.getOwner(), event.getAttackerId());
		}
	}

	public void deleteEnemy(Wsession session, CM_DELETE_ENEMY req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.deleteEnemy(player, req.getEnemyId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void addTargetToBlackList(Wsession session, CM_ADDTO_BLACKLIST req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.addTargetToBlackList(player, req.getTargetId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void deleteBlackList(Wsession session, CM_CANCEL_SHIELD req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.removeBlackList(player, req.getTargetId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	public void setDisbandAddFriends(Wsession session, CM_SET_DISBAND_ADD req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			contactService.setDisbandAddFriends(player, req.isDisbandAddFriend());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("发送聊天信息", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

}
