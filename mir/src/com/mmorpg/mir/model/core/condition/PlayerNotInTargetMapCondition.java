package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.model.Skill;
import com.windforce.common.utility.JsonUtils;

public class PlayerNotInTargetMapCondition extends AbstractCoreCondition {

	private Integer[] mapIds;
	
	private int errorCode;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (object instanceof Skill) {
			Skill skill = (Skill) object;
			player = (Player) skill.getEffector();
		}
		
		if (null == player) {
			this.errorObject(object);
		}

		boolean ret = isNotInMapIds(player.getMapId());
		if (!ret && errorCode != 0) {
			throw new ManagedException(errorCode);
		}
		return ret;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.mapIds = JsonUtils.string2Array(this.code, Integer.class);
		this.errorCode = resource.getSvrErrorCode();
	}

	private boolean isNotInMapIds(int targetMapId) {
		for (Integer mapId : mapIds) {
			if (targetMapId == mapId) {
				return false;
			}
		}
		return true;
	}
}
