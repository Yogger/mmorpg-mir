package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;

public class RankPositionCondition extends AbstractCoreCondition {

	private int low;

	private int high;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		
		RankType type = RankType.valueOf(code).getCountryRank(player.getCountryValue());

		return WorldRankManager.getInstance().verifyInRank(player, type, low, high);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow() - 1; // 排行榜数据从0开始的
		this.high = resource.getHigh() - 1;
	}

}
