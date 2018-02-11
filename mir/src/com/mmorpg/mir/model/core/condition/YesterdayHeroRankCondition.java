package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;

/**
 * 昨日英雄排行榜
 * 
 * @author 37.com
 * 
 */
public class YesterdayHeroRankCondition extends AbstractCoreCondition {

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

		RankType rankType = WorldRankManager.getInstance().getHeroType(player.getCountryValue(), false);

		return WorldRankManager.getInstance().verifyInRank(player, rankType, high, low);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

}
