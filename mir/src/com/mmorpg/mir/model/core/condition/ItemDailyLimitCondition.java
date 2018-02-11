package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.JsonUtils;

/**
 * code 配物品的ID value 配物品限制的数量
 * 
 * @author zouqiang
 */
public class ItemDailyLimitCondition extends AbstractCoreCondition {

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
		if (count + total > value) {
			throw new ManagedException(ManagedErrorCode.ITEM_USE_LIMIT);
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
