package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.JsonUtils;

/**
 * VIP版每日道具使用限制
 */
public class ItemDailyLimitVipCondition extends AbstractCoreCondition {

	private int count;
	private String[] items;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		int total = player.getPack().getTodayUseSituation(items);
		int extraCount = 0;
		for (String itemKey: items) {
			Integer count = player.getVip().getResource().getItemDailyExtraCount().get(itemKey);
			if (count != null) {
				extraCount += count;
			}
		}
		if (count + total > (value + extraCount)) {
			throw new ManagedException(ManagedErrorCode.ITEM_VIP_USE_LIMIT);
		}

		return true;
	}

	@Override
	protected void init() {
		super.init();
		items = JsonUtils.string2Array(code, String.class);
	}
	
	@Override
	protected void calculate(int num) {
		count = num;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
