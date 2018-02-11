package com.mmorpg.mir.model.exchange.manager;

import com.mmorpg.mir.model.exchange.core.Exchange;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.purse.model.CurrencyType;

public interface IExchangeManager {
	void registerExchange(final Player player1, final Player player2);

	Exchange getCurrentParnterExchange(Player player);

	void addCurrency(Player activePlayer, CurrencyType currencyType, int amount);

	void addItem(Player activePlayer, int index, AbstractItem item);

	void removeItem(Player activePlayer, int index);

	void exchangeItem(Player activePlayer, int packIndex, int exIndex, AbstractItem item);

	void releaseLock(Exchange exchange, Player activePlayer, Player partner);

	void lockExchange(Player activePlayer, boolean lock);

	void cancelExchange(Player activePlayer, int code);

	void confirmExchange(Player activePlayer);
}
