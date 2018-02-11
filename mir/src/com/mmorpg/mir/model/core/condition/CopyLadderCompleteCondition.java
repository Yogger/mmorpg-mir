package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.event.LadderNewRecordEvent;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 副本完成次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CopyLadderCompleteCondition extends AbstractCoreCondition implements QuestCondition {

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
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(code, true);
		if (resource.getType() != CopyType.LADDER) {
			throw new RuntimeException(resource.getId() + "不是爬塔副本!");
		}
		if (player.getCopyHistory().getLadderHisCompleteIndex() >= resource.getIndex()) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COPY_LADDER_NOT_COMPLETED);
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { LadderNewRecordEvent.class };
	}
}
