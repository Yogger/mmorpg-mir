package com.mmorpg.mir.model.core.condition;

import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class CopyTypeEnterCDCondition extends AbstractCoreCondition {

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

		for (Map.Entry<String, Long> entry : player.getCopyHistory().getLastEnterTime().entrySet()) {
			CopyResource copyResource = CopyManager.getInstance().getCopyResources().get(entry.getKey(), true);
			if (copyResource.getType().name().equals(code) && entry.getValue() > lastEnterTime) {
				lastEnterTime = entry.getValue();
			}
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
