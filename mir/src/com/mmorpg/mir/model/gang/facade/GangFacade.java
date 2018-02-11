package com.mmorpg.mir.model.gang.facade;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.packet.CM_ApplyCancel_Gang;
import com.mmorpg.mir.model.gang.packet.CM_ApplyList_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Apply_Gang;
import com.mmorpg.mir.model.gang.packet.CM_ChangePosition_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Create_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Cry_For_Help;
import com.mmorpg.mir.model.gang.packet.CM_DealApply_Gang;
import com.mmorpg.mir.model.gang.packet.CM_DealInvite_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Disband_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Expel_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Gang_DetailInfo;
import com.mmorpg.mir.model.gang.packet.CM_Gang_List;
import com.mmorpg.mir.model.gang.packet.CM_IMPEACH_GANG;
import com.mmorpg.mir.model.gang.packet.CM_Invite_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Join_Gang_Chat;
import com.mmorpg.mir.model.gang.packet.CM_PlayerSimple_Name;
import com.mmorpg.mir.model.gang.packet.CM_Quit_Gang;
import com.mmorpg.mir.model.gang.packet.CM_Set_AutoDeal;
import com.mmorpg.mir.model.gang.packet.CM_Set_GangInfo;
import com.mmorpg.mir.model.gang.packet.SM_Apply_Gang;
import com.mmorpg.mir.model.gang.packet.SM_ChangePosition_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Create_Gang;
import com.mmorpg.mir.model.gang.packet.SM_DealApply_Gang;
import com.mmorpg.mir.model.gang.packet.SM_DealInvite_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Disband_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Expel_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Gang_List;
import com.mmorpg.mir.model.gang.packet.SM_Quit_Gang;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.player.packet.SM_Player_Common;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.mmorpg.mir.model.world.packet.SM_PlayerSimple_All;
import com.mmorpg.mir.utils.CharCheckUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class GangFacade {
	private static Logger logger = Logger.getLogger(GangFacade.class);
	@Autowired
	private GangManager gangManager;

	@HandlerAnno
	public SM_Apply_Gang apply(TSession session, CM_Apply_Gang req) {
		SM_Apply_Gang sm = new SM_Apply_Gang();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			gangManager.apply(player, req.getId(), req.isByPlayerId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("申请入帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	// @HandlerAnno
	public void setGangInfo(TSession session, CM_Set_GangInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.setGangInfo(player, req.getInfo());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("设置帮派信息出现未知错误", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void setGang(TSession session, CM_Set_AutoDeal req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.setAutoDeal(player, req.isAutoDeal());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("设置帮派自动加入设置出现未知错误", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void cancelApply(TSession session, CM_ApplyCancel_Gang req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.applyCancel(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("取消申请帮派出现未知错误", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_ChangePosition_Gang changePosition(TSession session, CM_ChangePosition_Gang req) {
		SM_ChangePosition_Gang sm = new SM_ChangePosition_Gang();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			gangManager.changePosition(player, req.getTargetId(), req.getPosition());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("申请入帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Expel_Gang expel(TSession session, CM_Expel_Gang req) {
		SM_Expel_Gang sm = new SM_Expel_Gang();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			gangManager.expel(player, req.getTargetId());
			sm.setTargetId(req.getTargetId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("驱逐出帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Create_Gang createGange(TSession session, CM_Create_Gang req) {
		SM_Create_Gang sm = new SM_Create_Gang();
		try {
			if (req.getName() == null || req.getName().trim().isEmpty()) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NAME_IS_NULL);
			}

			// 这里直接先判断下名字
			if (!CharCheckUtil.checkString(req.getName())) {
				throw new ManagedException(ManagedErrorCode.WORDS_SENSITIVE);
			}

			Player player = SessionUtil.getPlayerBySession(session);
			req.setName("s" + player.getPlayerEnt().getServer() + "." + req.getName());
			Gang gang = gangManager.createGang(player, req.getName());
			sm.setGangVO(gang.creatVO());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("创建帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_DealApply_Gang dealApply(TSession session, CM_DealApply_Gang req) {
		SM_DealApply_Gang sm = new SM_DealApply_Gang();
		try {
			sm.setType(1);
			Player player = SessionUtil.getPlayerBySession(session);
			gangManager.dealApply(player, req.getPlayerId(), req.getOk() == 0 ? false : true);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("创建帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_DealApply_Gang invite(TSession session, CM_Invite_Gang req) {
		SM_DealApply_Gang sm = new SM_DealApply_Gang();
		try {
			sm.setType(2);
			Player player = SessionUtil.getPlayerBySession(session);
			if (!SessionManager.getInstance().isOnline(req.getId())) {
				sm.setCode(ManagedErrorCode.PLAYER_INLINE);
				return sm;
			}
			Player target = PlayerManager.getInstance().getPlayer(req.getId());
			if (GangOfWarManager.getInstance().getGangOfwars().get(player.getCountry().getId()).isWarring()) {
				sm.setCode(ManagedErrorCode.GANGOFWAR_FIGHTING);
				return sm;
			}
			gangManager.invite(player, target);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("创建帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void disband(TSession session, CM_Disband_Gang req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.disband(player);
		} catch (ManagedException e) {
			SM_Disband_Gang sm = new SM_Disband_Gang();
			sm.setCode(e.getCode());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
			logger.error("解散帮会", e);
		}
	}

	@HandlerAnno
	public SM_Quit_Gang quit(TSession session, CM_Quit_Gang req) {
		SM_Quit_Gang sm = new SM_Quit_Gang();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm.setLastQuitGangTime(gangManager.quit(player));
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("创建帮会", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_DealInvite_Gang dealInvite(TSession session, CM_DealInvite_Gang req) {
		SM_DealInvite_Gang sm = new SM_DealInvite_Gang();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			gangManager.dealInvite(player, req.getGangId(), req.isOk());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("邀请", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Gang_List getGangList(TSession session, CM_Gang_List req) {
		// 查询其他国家的家族
		if (req.getCountry() == -1) {
			Player player = SessionUtil.getPlayerBySession(session);
			String gangName = req.getGangName();
			if (gangName == null || gangName.isEmpty()) {
				return gangManager.getOtherCountryGangSimpleList(player);
			} else {
				return gangManager.getOtherCountryGangSimpleList(player, gangName);
			}
		}

		if (req.getCountry() != 0 && req.getGangName() != null) {
			return gangManager.getGangSimpleList(
					CountryManager.getInstance().getCountries().get(CountryId.valueOf(req.getCountry())),
					req.getGangName());
		}
		if (req.getCountry() != 0) {
			return gangManager.getGangSimpleList(CountryManager.getInstance().getCountries()
					.get(CountryId.valueOf(req.getCountry())));
		}

		return gangManager.getGangSimpleList();
	}

	@HandlerAnno
	public void getGangDetail(TSession session, CM_Gang_DetailInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.getGangDetailInfo(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取帮派详细信息出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getGangApplyList(TSession session, CM_ApplyList_Gang req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.getGangApplyList(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取帮派申请信息出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void joinGangChat(TSession session, CM_Join_Gang_Chat req) {
		Player player = SessionUtil.getPlayerBySession(session);
		Gang gang = player.getGang();
		if (gang == null) {
			return;
		}
		long now = System.currentTimeMillis();
		if (now - gang.getJoinChatTimeMills() >= GangManager.getInstance().JOIN_CHAT_CD.getValue()) {
			I18nUtils i18nUtils = I18nUtils.valueOf("304001");
			i18nUtils.addParm("family", I18nPack.valueOf(player.getGang().getName()));
			i18nUtils.addParm("familyId", I18nPack.valueOf(player.getGang().getId()));
			ChatManager.getInstance().sendSystem(6, i18nUtils, null, player.getCountry());
			gang.setJoinChatTimeMills(now);
		}
	}

	@HandlerAnno
	public void impeachGangMaster(TSession session, CM_IMPEACH_GANG req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.impeachGangMaster(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("弹劾族长", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void cry4Help(TSession session, CM_Cry_For_Help req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gangManager.askForHelp(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("国家召集令", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCanInvitePlayerByName(TSession session, CM_PlayerSimple_Name req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getName() == null || req.getName().length() == 0) {
			PacketSendUtility.sendPacket(player, new SM_Player_Common(ManagedErrorCode.NOT_FOUND_PLAYER));
			return;
		}
		ArrayList<PlayerSimpleInfo> resultSet = new ArrayList<PlayerSimpleInfo>();
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				GangManager.getInstance().GANG_DEAL_INVITE_CONDITION.getValue());
		for (Map.Entry<String, Player> entry : player.getCountry().getNameCivils().entrySet()) {
			if (entry.getValue().isInGang()) {
				continue;
			}
			if (entry.getKey().contains(req.getName()) && conditions.verify(entry.getValue(), false))
				resultSet.add(entry.getValue().createSimple());

			if (resultSet.size() >= GangManager.getInstance().FUZZY_SEARCH_LIMIT.getValue())
				break;
		}
		PacketSendUtility.sendPacket(player, SM_PlayerSimple_All.valueOf(resultSet));
	}

	@ReceiverAnno
	public void login(LoginEvent login) {
		// 通知帮会同伴该玩家上线
		Player player = PlayerManager.getInstance().getPlayer(login.getOwner());
		if (player.getPlayerGang().getGangId() != 0) {
			gangManager.playerLogin(player);
		}
	}

	@ReceiverAnno
	public void refreshLevel(LevelUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		long gangId = player.getPlayerGang().getGangId();
		if (gangId != 0) {
			gangManager.doLevelUpRefresh(gangId, player);
		}
	}

	@ReceiverAnno
	public void refreshVip(VipEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		long gangId = player.getPlayerGang().getGangId();
		if (gangId != 0) {
			gangManager.doVipRefresh(gangId, player);
		}
	}

	@ReceiverAnno
	public void refreshPromotion(PromotionEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		long gangId = player.getPlayerGang().getGangId();
		if (gangId != 0) {
			gangManager.doPromotionRefresh(gangId, player);
		}
	}
}
