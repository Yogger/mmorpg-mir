package com.mmorpg.mir.model.core.condition.map;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

/**
 * 当前地图鼓舞次数小于等于
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class MapCopyEncourageCountCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		WorldMapInstance instance = null;
		if (object instanceof Player) {
			instance = ((Player) object).getCopyHistory().getCurrentMapInstance();
		} else if (object instanceof WorldMapInstance) {
			instance = (WorldMapInstance) object;
		}
		if (instance == null) {
			this.errorObject(object);
		}

		if (instance.getCopyInfo() == null) {
			throw new RuntimeException("WorldMapInstance instance.getCopyInfo() == null !!!");
		}
		if (instance.getCopyInfo().getEncourgeCount() <= value) {
			return true;
		}
		return false;
	}
}
