package com.mmorpg.mir.model.beauty.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.beauty.manager.BeautyManager;
import com.mmorpg.mir.model.beauty.packet.CM_Beauty_Active;
import com.mmorpg.mir.model.beauty.packet.CM_Beauty_Fight;
import com.mmorpg.mir.model.beauty.packet.CM_Beauty_Linger;
import com.mmorpg.mir.model.beauty.packet.CM_Beauty_UnFight;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.relive.event.PlayerReliveEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class BeautyFacade {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private BeautyManager beautyManager;

	@HandlerAnno
	public void active(TSession session, CM_Beauty_Active req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			beautyManager.active(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void fight(TSession session, CM_Beauty_Fight req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			beautyManager.fight(player, req.getGirlId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void unFight(TSession session, CM_Beauty_UnFight req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			beautyManager.unFight(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void linger(TSession session, CM_Beauty_Linger req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			beautyManager.linger(player, req.getGirlId(), req.isAutoBuy());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getBeautyGirlPool().loginOrRelieve();

	}

	@ReceiverAnno
	public void playerRelive(PlayerReliveEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getBeautyGirlPool().loginOrRelieve();
	}

}
