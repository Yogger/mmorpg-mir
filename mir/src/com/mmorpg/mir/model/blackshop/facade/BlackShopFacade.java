package com.mmorpg.mir.model.blackshop.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.blackshop.BlackShopConfig;
import com.mmorpg.mir.model.blackshop.manager.BlackShopManager;
import com.mmorpg.mir.model.blackshop.packet.CM_BlackShop_Buy;
import com.mmorpg.mir.model.blackshop.packet.CM_BlackShop_Custom_Refresh;
import com.mmorpg.mir.model.blackshop.packet.CM_BlackShop_Query;
import com.mmorpg.mir.model.blackshop.packet.CM_BlackShop_System_Refresh;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class BlackShopFacade {
	private static final Logger logger = Logger.getLogger(BlackShopFacade.class);

	@Autowired
	private BlackShopManager blackShopManager;

	@Autowired
	private BlackShopConfig config;

	@HandlerAnno
	public void query(TSession session, CM_BlackShop_Query req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			blackShopManager.queryBlackShop(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黑市购买出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void buy(TSession session, CM_BlackShop_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getGridIndex() < 0 || req.getGridIndex() >= config.SHOP_GRID_COUNT.getValue()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
			return;
		}
		try {
			blackShopManager.buy(player, req.getGridIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黑市购买出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void customRefresh(TSession session, CM_BlackShop_Custom_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			blackShopManager.customRefresh(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黑市玩家刷新出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void systemRefresh(TSession session, CM_BlackShop_System_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			blackShopManager.systemRefresh(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黑市系统刷新出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

}
