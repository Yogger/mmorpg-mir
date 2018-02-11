package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.HorseNotLearnSKillCondition;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.sample.RewardItemSample;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class ItemResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, RewardSample> rewardSamples;

	@Static
	private Storage<String, ItemResource> itemResources;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;
	
	@Static("DEPOT:MAX_SIZE")
	public ConfigValue<Integer> DEPOT_MAX_SIZE;

	@Static("DEPOT:INIT_SIZE")
	public ConfigValue<Integer> DEPOT_INIT_SIZE;

	@Static("BAG:MAX_SIZE")
	public ConfigValue<Integer> BAG_MAX_SIZE;

	@Static("BAG:INIT_SIZE")
	public ConfigValue<Integer> BAG_INIT_SIZE;
	
	@Static("BAG:OPEN_CELL_STAT")
	public ConfigValue<Stat[]> BAG_OPEN_CELL_STAT;

	@Static("DEPOT:OPEN_CELL_STAT")
	public ConfigValue<Stat[]> DEPOT_OPEN_CELL_STAT;
	
	@Static("BAG:OPEN_CELL_MONEY")
	public ConfigValue<Integer[]> BAG_OPEN_CELL_MONEY;

	@Static("DEPOT:OPEN_CELL_MONEY")
	public ConfigValue<Integer[]> DEPOT_OPEN_CELL_MONEY;

	@Override
	public Class<?> getResourceClass() {
		return ItemResource.class;
	}

	@Override
	public void check() {
		List<String> errMes = New.arrayList();
		for (ItemResource itemResource : itemResources.getAll()) {
			if (itemResource.getReward() != null) {
				RewardSample reward = rewardSamples.get(itemResource.getReward(), false);
				if (reward == null) {
					throw new RuntimeException(String.format("ItemResource id[%s]  getReward[%s]找不到！",
							itemResource.getKey(), itemResource.getReward()));
				}
			}
			if (itemResource.getChooserReward() != null) {
				ChooserGroup chooser = chooserGroups.get(itemResource.getChooserReward(), false);
				if (chooser == null) {
					throw new RuntimeException(String.format("ItemResource id[%s]  getChooserReward[%s]找不到！",
							itemResource.getKey(), itemResource.getChooserReward()));
				}
			}
			
			CoreConditions useConditions = new CoreConditions();
			if (itemResource.getConditions() != null) {
				useConditions = CoreConditionManager.getCoreConditionResources(1, getCoreConditionResources(itemResource.getConditions()));
			}
			
			AbstractCoreCondition condition = useConditions.findConditionType(HorseNotLearnSKillCondition.class);
			if (condition != null) {
				int skillId = condition.getValue();
				RewardSample reward = rewardSamples.get(itemResource.getReward(), false);
				for (RewardItemSample sample : reward.getRewardItems()) {
					if (skillId != Integer.valueOf(sample.getCode())) {
						errMes.add(String.format("ItemResource id[%s]  getReward[%s]奖励的技能ID是[%s] 而是用条件里面配的是没学过技能ID[%s]",
								itemResource.getKey(), itemResource.getReward(), sample.getCode(), skillId));
					}
				}
			}
		}
		int canOpenBagSize = BAG_MAX_SIZE.getValue()
				- BAG_INIT_SIZE.getValue();
		int canOpenDepotSize = DEPOT_MAX_SIZE.getValue()
				- DEPOT_INIT_SIZE.getValue();
		if (canOpenBagSize < 0 || canOpenDepotSize < 0) {
			throw new RuntimeException(String.format("仓库或者背包的最大格子比初始的格子还小"));
		}
		if (BAG_OPEN_CELL_STAT.getValue().length < canOpenBagSize) {
			throw new RuntimeException(String.format("背包开格子属性少配了"));
		}
		if (BAG_OPEN_CELL_MONEY.getValue().length < canOpenBagSize) {
			throw new RuntimeException(String.format("背包开格子消耗元宝少配了"));
		}
		if (DEPOT_OPEN_CELL_STAT.getValue().length < canOpenDepotSize) {
			throw new RuntimeException(String.format("仓库开格子属性少配了"));
		}
		if (DEPOT_OPEN_CELL_MONEY.getValue().length < canOpenDepotSize) {
			throw new RuntimeException(String.format("仓库开格子消耗元宝少配了"));
		}
		
		if (!errMes.isEmpty()) {
			for (String err : errMes)
				System.err.println(err);
		}
		errMes.clear();
	}
}
