package com.mmorpg.mir.model.blackshop;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.blackshop.resource.BlackShopResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class BlackShopConfig {

	private static BlackShopConfig instance;

	@PostConstruct
	void init() {
		instance = this;
	}

	public static BlackShopConfig getInstance() {
		return instance;
	}

	@Static
	public Storage<String, BlackShopResource> blackShopStorage;

	/** 商店格子数 */
	@Static("BLACKSHOP:SHOP_GRID_COUNT")
	public ConfigValue<Integer> SHOP_GRID_COUNT;

	@Static("BLACKSHOP:SHOP_GRID_GROUPIDS")
	public ConfigValue<String[]> SHOP_GRID_GROUPIDS;

	public boolean isExistGoodGroupId(String groupId) {
		String[] goodChoserIds = SHOP_GRID_GROUPIDS.getValue();
		for (String id : goodChoserIds) {
			if (id.equals(groupId)) {
				return true;
			}
		}
		return false;
	}

	/** 需要广播的品质 */
	@Static("BLACKSHOP:SHOP_NOTICE_ITEM_QUALITY")
	public ConfigValue<Integer> SHOP_NOTICE_ITEM_QUALITY;

	/** 刷新优先消耗的道具id */
	@Static("BLACKSHOP:SHOP_REFRESH_PRIORITY_ITEM_ACTION")
	public ConfigValue<String> SHOP_REFRESH_PRIORITY_ITEM_ACTION;

	/** 刷新优先消耗的道具数量 */
	@Static("BLACKSHOP:SHOP_REFRESH_PRIORITY_ITEM_ACTION_COUNT")
	public ConfigValue<Integer> SHOP_REFRESH_PRIORITY_ITEM_ACTION_COUNT;

	/** 刷新消耗的元宝 */
	@Static("BLACKSHOP:SHOP_REFRESH_ACTIONIDS")
	private ConfigValue<String[]> SHOP_REFRESH_ACTIONIDS;

	private CoreActions refreshActions;

	public CoreActions getRefreshActions() {
		if (null == this.refreshActions) {
			this.refreshActions = CoreActionManager.getInstance().getCoreActions(1, SHOP_REFRESH_ACTIONIDS.getValue());
		}
		return this.refreshActions;
	}

	/** 系统刷新间隔（分钟） */
	@Static("BLACKSHOP:BLACK_MARKET_REFRESH")
	public ConfigValue<Integer> SHOP_REFRESH_INTERVAL_TIME;

	@Static("BLACKSHOP:OPEN_CONDS")
	private ConfigValue<String[]> OPEN_CONDS;

	private CoreConditions openConds;

	public CoreConditions getOpenConditions() {
		if (null == openConds) {
			openConds = CoreConditionManager.getInstance().getCoreConditions(1, OPEN_CONDS.getValue());
		}
		return openConds;
	}
}
