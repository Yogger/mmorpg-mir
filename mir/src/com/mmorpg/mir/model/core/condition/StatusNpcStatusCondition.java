package com.mmorpg.mir.model.core.condition;

import java.util.List;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 玩家当前地图NPC状态
 * 
 * @author Kuang Hao
 * @since v1.0 2014-10-17
 * 
 */
public class StatusNpcStatusCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getPosition().getMapRegion() == null) {
			return false;
		}
		List<StatusNpc> npcs = player.getPosition().getMapRegion().getParent().findObjectByType(ObjectType.STATUS_NPC);
		boolean found = false;
		for (StatusNpc n : npcs) {
			if (n.getObjectKey().equals(code)) {
				found = true;
				if (n.getStatus() != value) {
					throw new ManagedException(ManagedErrorCode.STATUS_NPC_ERROR);
				}
			}
		}
		return found;
	}
}
