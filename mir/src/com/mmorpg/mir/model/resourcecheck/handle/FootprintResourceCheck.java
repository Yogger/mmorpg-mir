package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class FootprintResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, RewardSample> rewardSamples;

	@Static
	private Storage<String, ItemResource> itemResources;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;

	@Override
	public Class<?> getResourceClass() {
		return ItemResource.class;
	}

	@Override
	public void check() {
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
		}
	}
}
