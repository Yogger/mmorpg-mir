package com.mmorpg.mir.model.country.controllers;

import org.apache.commons.lang.time.DateUtils;

import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class CountryArmyFlagController extends CountryNpcController {

	@Override
	public void onSpawn(int mapId, int instanceId) {
		super.onSpawn(mapId, instanceId);
		scheduleDespawn();
	}

	private void scheduleDespawn() {
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(
				new Runnable() {

					@Override
					public void run() {
						CountryArmyFlagController.this.delete();
					}
				},
				ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_PERIOD.getValue()
						* DateUtils.MILLIS_PER_SECOND,
				ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_PERIOD.getValue()
						* DateUtils.MILLIS_PER_SECOND);
	}
}
