package com.mmorpg.mir.model.exchange.manager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.consumable.CoreActionType;
import com.mmorpg.mir.model.exchange.core.Exchange;
import com.mmorpg.mir.model.exchange.core.ExchangeItem;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_RealConfirm;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_RealLock;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Start;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Stop;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Success;
import com.mmorpg.mir.model.exchange.packet.SM_Exchange_Update;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.event.CollectItemsEvent;
import com.mmorpg.mir.model.item.packet.SM_Packet_Update;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.purse.CurrencyUtils;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.purse.packet.SM_Currency;
import com.mmorpg.mir.model.restrictions.RestrictionsManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.New;

public class ExchangeManager {

	private Map<Long, Exchange> exchanges = new ConcurrentHashMap<Long, Exchange>();
	/** 交易的双方ID */
	private Map<Long, Long> exchangeStatus = new ConcurrentHashMap<Long, Long>();
	/** 请求交易方的请求时间 */
	private Map<Long, Long> exchangeTime = new ConcurrentHashMap<Long, Long>();

	private static final byte SELF = 0;
	private static final byte PARTNER = 1;

	private static final class SingletonHolder {
		private static final ExchangeManager INSTANCE = new ExchangeManager();
	}

	public static ExchangeManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void accessExchangeState(Player requester, Player responder, long overTime) {
		exchangeStatus.put(requester.getObjectId(), responder.getObjectId());
		exchangeStatus.put(responder.getObjectId(), requester.getObjectId());
		exchangeTime.put(requester.getObjectId(), overTime);
		exchangeTime.put(responder.getObjectId(), overTime);
	}

	public boolean isDuringExchangeStatus(Player player) {
		checkExchangeRequestStatus(player);
		return exchangeStatus.containsKey(player.getObjectId());
	}

	private void checkExchangeRequestStatus(Player player) {
		Long time = exchangeTime.get(player.getObjectId());
		if (time == null || time < System.currentTimeMillis()) {
			removeExchangeState(player);
		}
	}

	public void removeExchangeState(Player player) {
		Long oppnent = exchangeStatus.get(player.getObjectId());
		if (oppnent != null) {
			exchangeStatus.remove(oppnent);
			exchangeTime.remove(oppnent);
		}
		exchangeStatus.remove(player.getObjectId());
		exchangeTime.remove(player.getObjectId());
	}

	/**
	 * @param objectId
	 * @param objectId2
	 */
	public void registerExchange(final Player player1, final Player player2) {
		if (!validateParticipants(player1, player2))
			return;

		player1.setTrading(true);
		player2.setTrading(true);

		exchanges.put(player1.getObjectId(), new Exchange(player1, player2));
		exchanges.put(player2.getObjectId(), new Exchange(player2, player1));

		PacketSendUtility.sendPacket(player2,
				SM_Exchange_Start.valueOf(player1.getName(), player1.getLevel(), player1.getPlayerEnt().getServer()));
		PacketSendUtility.sendPacket(player1,
				SM_Exchange_Start.valueOf(player2.getName(), player2.getLevel(), player2.getPlayerEnt().getServer()));

	}

	/**
	 * @param player1
	 * @param player2
	 */
	private boolean validateParticipants(Player player1, Player player2) {
		return RestrictionsManager.canTrade(player1) && RestrictionsManager.canTrade(player2);
	}

	private Player getCurrentParter(Player player) {
		Exchange exchange = exchanges.get(player.getObjectId());
		return exchange != null ? exchange.getTargetPlayer() : null;
	}

	/**
	 * 
	 * @param player
	 * @return Exchange
	 */
	private Exchange getCurrentExchange(Player player) {
		return exchanges.get(player.getObjectId());
	}

	/**
	 * 
	 * @param player
	 * @return Exchange
	 */
	public Exchange getCurrentParnterExchange(Player player) {
		Player partner = getCurrentParter(player);
		return partner != null ? getCurrentExchange(partner) : null;
	}

	public void addCurrency(Player activePlayer, CurrencyType currencyType, int amount) {
		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentParnterExchange(activePlayer);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (partnerExchange.isConfirmed())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_CONFRIMD_ERROR);

		if (currencyType != CurrencyType.GOLD) {
			throw new ManagedException(ManagedErrorCode.CAN_NOT_TRADING_ITEM);
		}

		// 判断下玩家的真实元宝是否足够
		if (!CoreConditionType.createGoldCondition(amount).verify(activePlayer))
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		Player partner = getCurrentParter(activePlayer);

		currentExchange.getCurrencys().put(currencyType.getValue(), amount);

		releaseLock(currentExchange, activePlayer, partner);

		sendUpdate(currentExchange, activePlayer, partner);
	}

	/**
	 * @param activePlayer
	 * @param itemObjId
	 * @param itemCount
	 */
	public void addItem(Player activePlayer, int index, AbstractItem item) {
		Player partner = getCurrentParter(activePlayer);
		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentParnterExchange(activePlayer);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (!currentExchange.hasEmpty())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_IS_FULL);

		if (currentExchange.contains(index))
			throw new ManagedException(ManagedErrorCode.IN_TRADING_EXIST_ERROR);

		if (partnerExchange.isConfirmed())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_CONFRIMD_ERROR);

		currentExchange.addItem(ExchangeItem.valueOf(index, item));

		releaseLock(currentExchange, activePlayer, partner);

		sendUpdate(currentExchange, activePlayer, partner);

	}

	public void removeItem(Player activePlayer, int index) {
		Player partner = getCurrentParter(activePlayer);
		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentParnterExchange(activePlayer);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (partnerExchange.isConfirmed())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_CONFRIMD_ERROR);

		currentExchange.removeItemByIndex(index);

		releaseLock(currentExchange, activePlayer, partner);

		sendUpdate(currentExchange, activePlayer, partner);
	}

	public void exchangeItem(Player activePlayer, int packIndex, int exIndex, AbstractItem item) {

		Player partner = getCurrentParter(activePlayer);

		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentParnterExchange(activePlayer);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (partnerExchange.isConfirmed())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_CONFRIMD_ERROR);

		if (currentExchange.contains(packIndex))
			throw new ManagedException(ManagedErrorCode.IN_TRADING_EXIST_ERROR);

		currentExchange.setItem(exIndex, ExchangeItem.valueOf(packIndex, item));

		releaseLock(currentExchange, activePlayer, partner);

		sendUpdate(currentExchange, activePlayer, partner);
	}

	private void sendUpdate(Exchange exchange, Player activePlayer, Player partner) {
		Map<Integer, Object> exchangeUpdate = exchange.collectUpdate();
		Map<Integer, Integer> currencys = exchange.getCurrencys();

		PacketSendUtility.sendPacket(activePlayer, SM_Exchange_Update.valueOf(SELF, exchangeUpdate, currencys));
		PacketSendUtility.sendPacket(partner, SM_Exchange_Update.valueOf(PARTNER, exchangeUpdate, currencys));
	}

	public void releaseLock(Exchange exchange, Player activePlayer, Player partner) {
		if (exchange.isLocked()) {
			exchange.setLocked(Boolean.FALSE);
			PacketSendUtility.sendPacket(activePlayer, SM_Exchange_RealLock.valueOf(SELF, Boolean.FALSE));
			PacketSendUtility.sendPacket(partner, SM_Exchange_RealLock.valueOf(PARTNER, Boolean.FALSE));
		}
	}

	public void lockExchange(Player activePlayer, boolean lock) {
		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentParnterExchange(activePlayer);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if (partnerExchange.isConfirmed())
			throw new ManagedException(ManagedErrorCode.IN_TRADING_CONFRIMD_ERROR);

		currentExchange.setLocked(lock);
		Player currentParter = getCurrentParter(activePlayer);
		PacketSendUtility.sendPacket(activePlayer, SM_Exchange_RealLock.valueOf(SELF, lock));
		PacketSendUtility.sendPacket(currentParter, SM_Exchange_RealLock.valueOf(PARTNER, lock));
	}

	/**
	 * @param activePlayer
	 */
	public void cancelExchange(Player activePlayer, int code) {
		Player currentParter = getCurrentParter(activePlayer);
		cleanupExchanges(activePlayer, currentParter);
		removeExchangeState(activePlayer);
		PacketSendUtility.sendPacket(activePlayer, SM_Exchange_Stop.valueOf(code));
		PacketSendUtility.sendPacket(currentParter, SM_Exchange_Stop.valueOf(code));
	}

	/**
	 * @param activePlayer
	 */
	synchronized public void confirmExchange(Player activePlayer) {
		Player currentPartner = getCurrentParter(activePlayer);

		Exchange currentExchange = getCurrentExchange(activePlayer);
		Exchange partnerExchange = getCurrentExchange(currentPartner);

		if (currentExchange == null || partnerExchange == null)
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);

		if ((!currentExchange.isLocked()) || (!partnerExchange.isLocked()))
			throw new ManagedException(ManagedErrorCode.IN_TRADING_NOT_LOCK_ERROR);

		currentExchange.confirm();

		if (getCurrentExchange(currentPartner).isConfirmed()) {
			performTrade(activePlayer, currentPartner);
			return;
		}

		PacketSendUtility.sendPacket(activePlayer, SM_Exchange_RealConfirm.valueOf(SELF));
		PacketSendUtility.sendPacket(currentPartner, SM_Exchange_RealConfirm.valueOf(PARTNER));
	}

	private void performTrade(Player activePlayer, Player currentPartner) {
		int sponsorCode = validateExchange(activePlayer, currentPartner);
		int acceptCode = validateExchange(currentPartner, activePlayer);
		if (sponsorCode < 0 || acceptCode < 0) {
			Player currentParter = getCurrentParter(activePlayer);
			cleanupExchanges(activePlayer, currentParter);
			if (sponsorCode < 0) {
				PacketSendUtility.sendPacket(activePlayer,
						SM_Exchange_Stop.valueOf(ManagedErrorCode.TRADING_SELF_PACK_FULL));
				PacketSendUtility.sendPacket(currentParter,
						SM_Exchange_Stop.valueOf(ManagedErrorCode.TRADING_OTHER_PACK_FULL));
			} else {
				PacketSendUtility.sendPacket(activePlayer,
						SM_Exchange_Stop.valueOf(ManagedErrorCode.TRADING_OTHER_PACK_FULL));
				PacketSendUtility.sendPacket(currentParter,
						SM_Exchange_Stop.valueOf(ManagedErrorCode.TRADING_SELF_PACK_FULL));
			}
			return;
		}

		PacketSendUtility.sendPacket(activePlayer, SM_Exchange_Success.valueOf());
		PacketSendUtility.sendPacket(currentPartner, SM_Exchange_Success.valueOf());

		Exchange exchange1 = getCurrentExchange(activePlayer);
		Exchange exchange2 = getCurrentExchange(currentPartner);

		cleanupExchanges(activePlayer, currentPartner);

		removeItemsFromPack(activePlayer, currentPartner, exchange1);
		removeItemsFromPack(currentPartner, activePlayer, exchange2);

		putItemToInventory(currentPartner, activePlayer, exchange1);
		putItemToInventory(activePlayer, currentPartner, exchange2);
	}

	/**
	 * 
	 * @param activePlayer
	 * @param currentPartner
	 */
	private void cleanupExchanges(Player activePlayer, Player currentPartner) {
		cleanUpExchange(activePlayer);
		cleanUpExchange(currentPartner);
	}

	private void cleanUpExchange(Player player) {
		if (player != null) {
			Exchange exchange = exchanges.remove(player.getObjectId());
			if (exchange != null)
				exchange.cancelAllTask();
			player.setTrading(false);
		}
	}

	/**
	 * @param player
	 * @param exchange
	 */
	private void removeItemsFromPack(Player player, Player partner, Exchange exchange) {
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.EXCHANGE, SubModuleType.EXCAHNGE_ACT,
				partner.getObjectId() + "|" + partner.getName());
		for (ExchangeItem item : exchange.getItems()) {
			if (item != null && item.getItem() != null) {
				AbstractItem eItem = player.getPack().removeItemByIndex(item.getIndex());
				LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, -1, eItem.getSize(), eItem,
						player.getPack().getItemSizeByKey(eItem.getKey()));
			}
		}
		for (Entry<Integer, Integer> entry : exchange.getCurrencys().entrySet()) {
			if (entry.getValue() > 0) {
				CurrencyType currencyType = CurrencyType.valueOf(entry.getKey());
				CoreActionType.createCurrencyCondition(currencyType, entry.getValue()).tradeAct(player, moduleInfo);
			}
		}
	}

	/**
	 * @param activePlayer
	 * @param currentPartner
	 * @return
	 */
	private int validateExchange(Player activePlayer, Player currentPartner) {
		Exchange exchange1 = getCurrentExchange(activePlayer);
		Exchange exchange2 = getCurrentExchange(currentPartner);
		return validateInventorySize(activePlayer, exchange1, exchange2.getSize()); // &&
																					// validateInventorySize(currentPartner,
																					// exchange2,
																					// exchange1.getSize());
	}

	private int validateInventorySize(Player activePlayer, Exchange exchange, int needSlots) {
		int numberOfFreeSlots = activePlayer.getPack().getEmptySize();
		boolean success = numberOfFreeSlots >= needSlots;
		if (success) {
			for (Entry<Integer, Integer> entry : exchange.getCurrencys().entrySet()) {
				try {
					if (entry.getValue() > 0) {
						CurrencyType type = CurrencyType.valueOf(entry.getKey());
						if (type.equals(CurrencyType.GOLD)) {
							if (!CoreConditionType.createGoldCondition(entry.getValue()).verify(activePlayer)) {
								return type.getErrorCode();
							}
						} else {
							if (!CoreConditionType.createCurrencyCondition(type, entry.getValue())
									.verify(activePlayer)) {
								return type.getErrorCode();
							}
						}
					}
				} catch (ManagedException e) {
					return e.getCode();
				}
			}
		} else {
			return ManagedErrorCode.PACK_FULL;
		}
		return 0;
	}

	private void putItemToInventory(Player player, Player partner, Exchange exchange) {
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.EXCHANGE, SubModuleType.EXCAHNGE_ACT,
				partner.getObjectId() + "|" + partner.getName());
		ArrayList<AbstractItem> addItems = New.arrayList();
		for (ExchangeItem item : exchange.getItems()) {
			if (item != null && item.getItem() != null) {
				AbstractItem eItem = item.getItem();
				player.getPack().addItems(false, item.getItem());
				LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, 1, eItem.getSize(), eItem,
						player.getPack().getItemSizeByKey(eItem.getKey()));
				addItems.add(eItem);
			}
		}
		for (Entry<Integer, Integer> entry : exchange.getCurrencys().entrySet()) {
			if (entry.getValue() > 0) {
				CurrencyType currencyType = CurrencyType.valueOf(entry.getKey());
				CurrencyUtils.getInstance().incomeByLog(player, currencyType, entry.getValue(), moduleInfo);
			}
		}
		EventBusManager.getInstance().submit(CollectItemsEvent.valueOf(player, addItems));
		PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_Currency.valueOf(player));
	}
}
