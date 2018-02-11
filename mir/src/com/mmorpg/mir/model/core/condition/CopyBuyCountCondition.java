package com.mmorpg.mir.model.core.condition;

import org.springframework.util.StringUtils;

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
public class CopyBuyCountCondition extends AbstractCoreCondition {

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
		if (!player.getCopyHistory().getBuyCounts().containsKey(code)) {
			return true;
		}
		if (player.getCopyHistory().getBuyCounts().containsKey(code)) {
			count = player.getCopyHistory().getBuyCounts().get(code);
		}
		count -= player.getVip().getResource().getExpCopyExCount();
		if (count <= value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COPY_BUY_COUNT);
	}

	public static void main(String[] args) {
		String str = "$VIP.value";
		System.out.println(str.split("\\.")[0]);
		String type = StringUtils.trimLeadingCharacter(str.split("\\.")[0], '$');
		System.out.println(type);
	}
}
