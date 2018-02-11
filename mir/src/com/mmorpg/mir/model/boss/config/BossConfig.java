package com.mmorpg.mir.model.boss.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.resource.BossStoreCoinResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class BossConfig {

	private static BossConfig self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static BossConfig getInstance() {
		return self;
	}
	
	@Static
	public Storage<String, BossStoreCoinResource> bossStoreCoinsResources;

	/** BOSS伤害面板显示人数 */
	@Static("BOSS:SHOW_DAMAGERANK_SIZE")
	public ConfigValue<Integer> SHOW_DAMAGERANK_SIZE;
	
	@Static("BOSS:BOSS_HOME_MAPIDS")
	public ConfigValue<Integer[]> BOSS_HOME_MAPIDS;
	
	@Static("BOSS:COINS_LAST_ATTACK_PERIOD")
	public ConfigValue<Integer> COINS_LAST_ATTACK_PERIOD;
	
/*	@Static("BOSS:COINS_ONLINE_GAIN_PERIOD")
	public ConfigValue<Integer> COINS_ONLINE_GAIN_PERIOD;*/
	
/*	@Static("BOSS:COINS_ONLINE_REWARD_ID")
	public ConfigValue<String> COINS_ONLINE_REWARD_ID;*/
	
	@Static("BOSS:COINS_OPEN_MODULE_ID")
	public ConfigValue<String> COINS_OPEN_MODULE_ID;
	
	@Static("BOSS:COINS_INIT_LEVEL_ID")
	public ConfigValue<String> COINS_INIT_LEVEL_ID;
	
	public boolean isInBossHomeMap(int mapId) {
		for (Integer map : BOSS_HOME_MAPIDS.getValue()) {
			if (map.intValue() == mapId) {
				return true;
			}
		}
		return false;
	}
	
	public static final int GIFT_BAG_MAX_SIZE = 10;
	
	public BossStoreCoinResource getBossStoreCoinResource(String id) {
		return bossStoreCoinsResources.get(id, true);
	}
	
}
