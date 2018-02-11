package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 副本进入次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CopyEnterCountCondition extends AbstractCoreCondition {

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
		int count = 0;
		if (player.getCopyHistory().getTodayEnterHistory().containsKey(code)) {
			count += player.getCopyHistory().getTodayEnterHistory().get(code);
		}
		if (player.getCopyHistory().getBuyCounts().containsKey(code)) {
			count -= player.getCopyHistory().getBuyCounts().get(code);
		}
		if (count < value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_COPY);
	}
}
