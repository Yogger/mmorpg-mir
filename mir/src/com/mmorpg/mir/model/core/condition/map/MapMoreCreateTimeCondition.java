package com.mmorpg.mir.model.core.condition.map;

import java.util.Map;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.world.WorldMapInstance;

/**
 * 当前地图创建时间
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class MapMoreCreateTimeCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		WorldMapInstance instance = null;
		if (object instanceof Map) {
			instance = (WorldMapInstance) ((Map<String, Object>) object).get(TriggerContextKey.MAP_INSTANCE);
		}
		if (object instanceof VisibleObject) {
			VisibleObject vo = ((VisibleObject) object);
			if (vo.getPosition() != null && vo.getActiveRegion() != null) {
				instance = ((VisibleObject) object).getActiveRegion().getParent();
			} else {
				return false;
			}
		}

		if (object instanceof WorldMapInstance) {
			instance = (WorldMapInstance) object;
		}
		if (instance == null) {
			this.errorObject(object);
		}
		if ((System.currentTimeMillis() - instance.getCreateTime()) >= value) {
			return true;
		}
		return false;
	}

}
