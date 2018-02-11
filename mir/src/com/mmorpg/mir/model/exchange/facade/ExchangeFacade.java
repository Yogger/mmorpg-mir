package com.mmorpg.mir.model.exchange.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Add_Currency;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Add_Item;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Cancel;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Confirm;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Ex_Item;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Lock;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Remove_Item;
import com.mmorpg.mir.model.exchange.packet.CM_Exchange_Request;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Add_Currency;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Add_Item;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Cancel;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Confirm;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Ex_Item;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Lock;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Remove_Item;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Request;
import com.mmorpg.mir.model.exchange.service.ExchangeService;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ExchangeFacade {

	@Autowired
	private ExchangeService exchangeService;

	private static final Logger logger = Logger.getLogger(ExchangeFacade.class);

	@HandlerAnno
	public SM_Exchange_Request exchangeRequest(TSession session, CM_Exchange_Request req) {
		SM_Exchange_Request sm = new SM_Exchange_Request();

		if (!SessionManager.getInstance().isOnline(req.getObjId())) {
			sm.setCode(ManagedErrorCode.PLAYER_INLINE);
			return sm;
		}
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeRequest(player, req.getObjId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家发起交易请求", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Add_Currency exchangeAddCurrency(TSession session, CM_Exchange_Add_Currency req) {
		SM_Exchange_Add_Currency sm = new SM_Exchange_Add_Currency();
		if (req.getAmount() < 0 || req.getAmount() > 999999) {
			sm.setCode(ManagedErrorCode.ERROR_MSG);
			return sm;
		}
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeAddCurrency(player, req.getCurrencyType(), req.getAmount());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家添加交易货币", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Add_Item exchangeAddItem(TSession session, CM_Exchange_Add_Item req) {
		SM_Exchange_Add_Item sm = new SM_Exchange_Add_Item();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeAddItem(player, req.getPackIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家添加交易道具", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Remove_Item exchangeRemoveItem(TSession session, CM_Exchange_Remove_Item req) {
		SM_Exchange_Remove_Item sm = new SM_Exchange_Remove_Item();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeRemoveItem(player, req.getExIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家删除交易道具", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Ex_Item exchangeExItem(TSession session, CM_Exchange_Ex_Item req) {
		SM_Exchange_Ex_Item sm = new SM_Exchange_Ex_Item();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeExItem(player, req.getPackIndex(), req.getExIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家交换交易道具", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Lock exchangeLock(TSession session, CM_Exchange_Lock req) {
		SM_Exchange_Lock sm = new SM_Exchange_Lock();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeLock(player, req.isLock());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家锁定交易", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Cancel exchangeCancel(TSession session, CM_Exchange_Cancel req) {
		SM_Exchange_Cancel sm = new SM_Exchange_Cancel();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeCancel(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家取消交易", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Exchange_Confirm exchangeConfirm(TSession session, CM_Exchange_Confirm req) {
		SM_Exchange_Confirm sm = new SM_Exchange_Confirm();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			exchangeService.exchangeConfirm(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家确定交易", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}
}
