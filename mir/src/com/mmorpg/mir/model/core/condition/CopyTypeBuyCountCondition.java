package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 副本进入次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CopyTypeBuyCountCondition extends AbstractCoreCondition {

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

		CopyType type = CopyType.typeOf(code);
		int count = player.getCopyHistory().getTotalTypeCopyBuyCount(type);
		if (type == CopyType.EXP) {
			// 如果是血战边疆 添加VIP次数
			count -= player.getVip().getResource().getExpCopyExCount();
		}
		if (count < value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COPY_BUY_COUNT);
	}
}
