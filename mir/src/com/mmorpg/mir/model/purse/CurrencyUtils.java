package com.mmorpg.mir.model.purse;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.purse.model.Purse;

/**
 * 消费工具类
 * 
 * @author Kuang Hao
 * @since v1.0 2013-2-19
 * 
 */
@Component
public class CurrencyUtils {

	private static CurrencyUtils self;

	public static CurrencyUtils getInstance() {
		return self;
	}

	@PostConstruct
	protected final void init() {
		self = this;
	}

	/**
	 * 消费
	 * 
	 * @param purse
	 *            钱包实体，注意锁并发
	 * @param type
	 *            货币类型
	 * @param value
	 *            值
	 * @return
	 */
	public void costByLog(Player player, CurrencyType type, int value, ModuleInfo module) {
		Purse purse = player.getPurse();
		if (value < 0) {
			throw new RuntimeException(String.format("costByLog currencyType [%s] value[%s] error.module[%s]!",
					new Object[] { type.name(), value, module }));
		}
		if (!purse.isEnough(type, value)) {
			throw new RuntimeException(String.format("not enough money! check error!type[%s],value[%s] module[%s]",
					new Object[] { type, value, module }));
		}
		long now = System.currentTimeMillis();

		if (type == CurrencyType.GOLD) {
			// 元宝特殊处理
			long remain = purse.cost(CurrencyType.INTER, value);
			// 内币日志
			if (value != remain) {
				LogManager.addCurrency(player, now, module, -1, CurrencyType.INTER.name(), (value - remain),
						purse.getValue(type));
			}
			if (remain > 0) {
				purse.cost(CurrencyType.GOLD, (int) remain);
				// 元宝日志
				LogManager.addCurrency(player, now, module, -1, CurrencyType.GOLD.name(), remain, purse.getValue(type));
			}
			player.getPlayerStat().setCurrentGold(purse.getValue(CurrencyType.GOLD));
			player.getPlayerStat().setCurrentInter(purse.getValue(CurrencyType.INTER));
		} else {
			purse.cost(type, value);
			//
			LogManager.addCurrency(player, now, module, -1, type.name(), value, purse.getValue(type));
		}
	}

	/**
	 * 收入
	 * 
	 * @param purse
	 *            钱包实体，注意锁并发
	 * @param type
	 *            货币类型
	 * @param value
	 *            值
	 * @return
	 */
	public void incomeByLog(Player player, CurrencyType type, int value, ModuleInfo module) {
		Purse purse = player.getPurse();
		if (value < 0) {
			throw new RuntimeException(String.format("incomeByLog currencyType [%s] value[%s] error.module[%s]!",
					new Object[] { type.name(), value, module }));
		}

		purse.add(type, value);
		if (type == CurrencyType.GOLD) {
			player.getPlayerStat().setCurrentGold(purse.getValue(CurrencyType.GOLD));
		} else if (type == CurrencyType.INTER) {
			player.getPlayerStat().setCurrentInter(purse.getValue(CurrencyType.INTER));
		}
		// 日志
		LogManager.addCurrency(player, System.currentTimeMillis(), module, 1, type.name(), value, purse.getValue(type));
	}

}
