package com.mmorpg.mir.model.gascopy.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.resource.anno.Static;

@Component
public class GasCopyMapConfig {

	private static GasCopyMapConfig INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static GasCopyMapConfig getInstance() {
		return INSTANCE;
	}
	
	@Static("GASCOPY:MAPID")
	public ConfigValue<Integer> MAPID;
	
	@Static("GASCOPY:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;
	
	@Static("GASCOPY:ENTER_CONDIDS")
	public ConfigValue<String[]> ENTER_CONDIDS;
	
	@Static("GASCOPY:ENTER_ACT_ITEMKEY")
	public ConfigValue<String> ENTER_ACT_ITEMKEY;
	
	@Static("GASCOPY:ENTER_ACT_ITEM_NUM")
	public ConfigValue<Integer[]> ENTER_ACT_ITEM_NUM;
	
	public Integer getEnterActItemNum(Player player) {
		int count = player.getGasCopy().getDailyEnterCount();
		if (count >= ENTER_ACT_ITEM_NUM.getValue().length || count < 0) {
			return null;
		}
		return ENTER_ACT_ITEM_NUM.getValue()[count];
	}
	
	// Vip rseource limit
	
	@Static("GASCOPY:PLAYER_GAS_VALUE_MAX")
	public ConfigValue<Integer> PLAYER_GAS_VALUE_MAX;
	
	// Obj resource addGasValue
	
	@Static("GASCOPY:PLAYER_DEAD_GAS_VALUE")
	public ConfigValue<Integer> PLAYER_DEAD_GAS_VALUE;

	@Static("GASCOPY:IN_MAP_GAS_VALUE")
	public ConfigValue<Integer> IN_MAP_GAS_VALUE;
	
	@Static("GASCOPY:IN_MAP_REWARDID")
	public ConfigValue<String> IN_MAP_REWARDID;
	
	@Static("GASCOPY:IN_MAP_REWARD_PERIOD")
	public ConfigValue<Integer> IN_MAP_REWARD_PERIOD;
	
	@Static("GASCOPY:GAS_FULL_LEAVE_TIME")
	public ConfigValue<Integer> GAS_FULL_LEAVE_TIME;
	
	@Static("GASCOPY:TRIGGER_LEAVE_TIME")
	public ConfigValue<Integer> TRIGGER_LEAVE_TIME;
	
	@Static("GASCOPY:BACKHOME_POINT")
	public ConfigValue<String> BACKHOME_POINT;
	
	@Static("GASCOPY:ENTER_ITEM_SHOP_ID")
	public ConfigValue<String> ENTER_ITEM_SHOP_ID;

	public boolean isInGasCopyMap(int mapId) {
		return mapId == MAPID.getValue();
	}
	
	public CoreConditions getEnterConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, ENTER_CONDIDS.getValue());
	}
	
}

