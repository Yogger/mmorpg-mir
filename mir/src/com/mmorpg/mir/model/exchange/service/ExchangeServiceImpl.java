package com.mmorpg.mir.model.exchange.service;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.complexstate.ComplexStateType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.exchange.manager.ExchangeManager;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_RJ_Request;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.RequestHandlerType;
import com.mmorpg.mir.model.gameobjects.RequestResponseHandler;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.packet.SM_Question_Request;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.anno.Static;

@Component
public class ExchangeServiceImpl implements ExchangeService {
	
	@Static("TRADE:WAIT_TIME")
	public ConfigValue<Integer> TRADE_WAIT_TIME;
	
	@Static("TRADE:TRADE_CONDITION")
	public ConfigValue<String[]> TRADE_CONDITION;

	public void exchangeRequest(final Player player, long objId) {
		check(player);
		
		if (player.isTrading() || ExchangeManager.getInstance().isDuringExchangeStatus(player))
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		
		CoreConditions condition = CoreConditionManager.getInstance().getCoreConditions(1, TRADE_CONDITION.getValue());
		if (!condition.verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.TRADE_CONDITION_NOT_SATISFY);
		}

		Player target = PlayerManager.getInstance().getPlayer(objId);
		check(target);
		
		if (player.getCountryValue() != target.getCountryValue()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_SAME);
		}
		
		if (!condition.verify(target, false)) {
			throw new ManagedException(ManagedErrorCode.OPPNENT_TRADE_CONDITION_NOT_SATISFY);
		}
		
		if (player == target)
			throw new ManagedException(ManagedErrorCode.IN_TRADING_SELF_ERROR);
		
		if (target.getRequester().hadRequest(RequestHandlerType.EXCHANGE_INVITE)) {
			throw new ManagedException(ManagedErrorCode.TARGET_TRADING_ERROR);
		}
		
		if (target.isTrading() || ExchangeManager.getInstance().isDuringExchangeStatus(target))
			throw new ManagedException(ManagedErrorCode.TARGET_TRADING_ERROR);

		if (target.getComplexState().isState(ComplexStateType.TRADE)) {
			throw new ManagedException(ManagedErrorCode.TARGET_DISBAND_TRADE_REQUEST);
		}

		final long overTime = System.currentTimeMillis() + TRADE_WAIT_TIME.getValue() * 1000;

		target.getRequester().putRequest(RequestHandlerType.EXCHANGE_INVITE, new RequestResponseHandler(player) {
			@Override
			public void denyRequest(Creature requester, Player responder) {
				ExchangeManager.getInstance().removeExchangeState(player);
				PacketSendUtility.sendPacket(player, SM_Exchange_RJ_Request.valueOf(responder.getName()));
			}

			@Override
			public void acceptRequest(Creature requester, Player responder) {
				Player player = (Player) requester;
				ExchangeManager.getInstance().removeExchangeState(player);
				if (!SessionManager.getInstance().isOnline(player.getObjectId())) {
					PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_INLINE);
					return;
				}
				if (!responder.isTrading()) {
					if (player.getLifeStats().isAlreadyDead()) {
						PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.OPPONENT_IS_ALEADY_DEAD);
						return;
					}
					if (responder.getLifeStats().isAlreadyDead()) {
						PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.DEAD_ERROR);
						return;
					}
					if (player.isTrading()) {
						PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.TARGET_TRADING_ERROR);
						return;
					}
					if (System.currentTimeMillis() <= overTime) {
						ExchangeManager.getInstance().registerExchange(player, responder);
					} else {
						PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.EXCHANGE_OVER_TIME);
					}
				}
			}

			@Override
			public boolean deprecated() {
				boolean isTimeOut = System.currentTimeMillis() > overTime;
				if (isTimeOut) {
					ExchangeManager.getInstance().removeExchangeState(player);
				}
				return isTimeOut;
			}
		});
		
		ExchangeManager.getInstance().accessExchangeState(player, target, overTime);
		// 这里向目标发送一个请求
		PacketSendUtility.sendPacket(target,
				SM_Question_Request.valueOf(RequestHandlerType.EXCHANGE_INVITE.getValue(), player, overTime));
	}

	public void exchangeAddCurrency(Player player, int currencyType, int amount) {
		check(player);

		if (amount < 0 || amount > 999999)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		CurrencyType type = CurrencyType.valueOf(currencyType);

		if (type != CurrencyType.GOLD)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().addCurrency(player, type, amount);
	}

	public void exchangeAddItem(Player player, int index) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		AbstractItem item = player.getPack().getItemByIndex(index);
		if (item == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (!item.canTrade())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().addItem(player, index, item);
	}

	public void exchangeLock(Player player, boolean lock) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().lockExchange(player, lock);
	}

	public void exchangeCancel(Player player) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().cancelExchange(player, 0);
	}

	public void exchangeConfirm(Player player) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().confirmExchange(player);
	}

	public void exchangeRemoveItem(Player player, int exIndex) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().removeItem(player, exIndex);
	}

	public void exchangeExItem(Player player, int packIndex, int exIndex) {
		check(player);

		if (!player.isTrading())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		AbstractItem item = player.getPack().getItemByIndex(packIndex);
		if (item == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		// Check Trade Hack
		if (!item.canTrade())
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		ExchangeManager.getInstance().exchangeItem(player, packIndex, exIndex, item);
	}

	private boolean check(Player player) {
		if (player == null)
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		else if (!player.isSpawned())
			throw new ManagedException(ManagedErrorCode.PLAYER_INLINE);
		else if (player.getLifeStats().isAlreadyDead())
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		return true;
	}
}
