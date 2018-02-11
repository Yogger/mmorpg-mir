package com.mmorpg.mir.model.shop.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.shop.packet.CM_ShopBuy;
import com.mmorpg.mir.model.shop.packet.SM_ShopBuy;
import com.mmorpg.mir.model.shop.packet.vo.ShoppingHistoryVO;
import com.mmorpg.mir.model.shop.service.ShopService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ShopFacade {
	private static Logger logger = Logger.getLogger(ShopFacade.class);
	@Autowired
	private ShopService shopService;

	@HandlerAnno
	public SM_ShopBuy buy(TSession session, CM_ShopBuy req) {
		SM_ShopBuy sm = new SM_ShopBuy();
		if (req.getCount() < 1 || req.getCount() > 9999) {
			sm.setCode(ManagedErrorCode.ERROR_MSG);
			return sm;
		}
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			shopService.buy(player, req.getId(), req.getCount());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("购买物品", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@ReceiverAnno
	public void refreshShopBuyLimit(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getShoppingHistory().refresh();
		PacketSendUtility.sendPacket(player, ShoppingHistoryVO.valueOf(player));
	}

	@ReceiverAnno
	public void loginEvent(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getShoppingHistory().refresh();
	}
}
