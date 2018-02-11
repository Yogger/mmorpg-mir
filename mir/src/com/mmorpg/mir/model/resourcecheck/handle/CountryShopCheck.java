package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.resource.CountryShopResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CountryShopCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, RewardSample> rewardSamples;

	@Static
	private Storage<String, CountryShopResource> countryShopResource;

	@Static
	private Storage<String, CoreConditionResource> conditionResources;

	@Static
	private Storage<String, CoreActionResource> actionResources;
	
	@Static
	private Storage<String, ChooserGroup> chooserGroups;
	
	@Static
	private Storage<String, Chooser> choosers;

	@Override
	public Class<?> getResourceClass() {
		return CountryShopResource.class;
	}

	@Override
	public void check() {

		for (CountryShopResource csr : countryShopResource.getAll()) {
			if (csr.getChooserGroupId() != null) {
				ChooserGroup group = chooserGroups.get(csr.getChooserGroupId(), true);
				for (String chooserId : group.getValueChoosers()) {
					Chooser chooser = choosers.get(chooserId, true);
					for (String rewardId: chooser.calcResult()) {
						RewardSample rs = rewardSamples.get(rewardId, false);
						if (rs == null) {
							throw new RuntimeException(String.format("CountryShopResource id[%s] getChooserGroupId[%s]不存在！ ",
									csr.getId(), csr.getChooserGroupId()));
						}
					}
				}
			}

			if (csr.getBuyCondtions() != null) {
				for (String cId : csr.getBuyCondtions()) {
					CoreConditionResource cr = conditionResources.get(cId, false);
					if (cr == null) {
						throw new RuntimeException(String.format("CountryShopResource id[%s] getBuyCondtions[%s]不存在！ ",
								csr.getId(), cId));
					}
				}
			}

			if (csr.getActions() != null) {
				for (String aId : csr.getActions()) {
					CoreActionResource ar = actionResources.get(aId, false);
					if (ar == null) {
						throw new RuntimeException(String.format("CountryShopResource id[%s] getActions[%s]不存在！ ",
								csr.getId(), aId));
					}
				}
			}
		}
	}
}
