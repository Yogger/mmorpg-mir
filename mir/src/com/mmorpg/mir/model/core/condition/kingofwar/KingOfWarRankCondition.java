package com.mmorpg.mir.model.core.condition.kingofwar;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;

/**
 * 咸阳战进行中
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-24
 * 
 */
public class KingOfWarRankCondition extends AbstractCoreCondition {

	private int low;
	private int high;

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

	@Override
	public boolean verify(Object object) {

		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		PlayerWarInfo pwi = KingOfWarManager.getInstance().getPlayerWarInfos().get(player.getObjectId());
		if (pwi == null) {
			return false;
		}
		int rank = KingOfWarManager.getInstance().getRankTemp().indexOf(pwi) + 1;
		if (low <= rank && rank <= high) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
