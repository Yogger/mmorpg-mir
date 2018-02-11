package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class BeautyGirlSumLevelCondition extends AbstractCoreCondition {

	private int low;
	private int high;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}

		int sumLevel = player.getBeautyGirlPool().getSumLevel();
		if (low <= sumLevel && sumLevel <= high) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.BEAUTY_GIRL_SUMLEVEL_NOT_VERIFY);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}
}
