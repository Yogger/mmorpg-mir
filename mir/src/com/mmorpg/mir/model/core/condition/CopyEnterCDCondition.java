package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 副本进入CD
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CopyEnterCDCondition extends AbstractCoreCondition {

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
		long cd = value;
		long lastEnterTime = 0;
		if (player.getCopyHistory().getLastEnterTime().containsKey(code)) {
			lastEnterTime = player.getCopyHistory().getLastEnterTime().get(code);
		}
		if ((lastEnterTime + cd) <= System.currentTimeMillis()) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COPY_ENTER_CD);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
