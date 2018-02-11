package com.mmorpg.mir.model.lifegrid;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.lifegrid.resource.LifeGridConvertResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class LifeGridConfig {

	private static LifeGridConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static LifeGridConfig getInstance() {
		return INSTANCE;
	}

	/** 命格装备栏大小 */
	@Static("LIFEGRID:LIFEGRID_EQUIP_PACK_SIZE")
	public ConfigValue<Integer> LIFEGRID_EQUIP_PACK_SIZE;

	/** 命格背包大小 */
	@Static("LIFEGRID:LIFEGRID_PACK_SIZE")
	public ConfigValue<Integer> LIFEGRID_PACK_SIZE;

	/** 命格仓库大小 */
	@Static("LIFEGRID:LIFEGRID_STORAGE_SIZE")
	public ConfigValue<Integer> LIFEGRID_STORAGE_SIZE;

	@Static
	public Storage<String, LifeGridConvertResource> convertStorage;

	/** 格子的开放等级 */
	@Static("LIFEGRID:OPENLEVEL")
	public ConfigValue<Integer[]> OPENLEVEL;

	public int getExtendSize(int curLevel, int lastLevel) {
		Integer[] list = OPENLEVEL.getValue();
		Arrays.sort(list);
		int result = 0;
		for (int v : list) {
			if (v > lastLevel && curLevel >= v) {
				result++;
			}
		}
		return result;
	}

	public ItemResource getCanUpLevelResource(List<ItemResource> sameTypeResources, int curlevel, int exp) {
		ItemResource r = null;
		for (ItemResource resource : sameTypeResources) {
			if (resource.getLifeGridLevel() < curlevel) {
				continue;
			}

			if (resource.getNeedExp() <= exp) {
				exp -= resource.getNeedExp();
				r = resource;
			}
		}
		return r;
	}

}
