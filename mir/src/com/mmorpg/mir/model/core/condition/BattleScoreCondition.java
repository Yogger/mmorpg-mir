package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class BattleScoreCondition extends AbstractCoreCondition {

	private int low;
	private int high;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		} else if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getGameStats().statSize() != 0) {
			int score = player.getGameStats().calcBattleScore();
			if (score >= this.low && score <= high) {
				return true;
			}
		}
		if (isThrowException()) {
			throw new ManagedException(ManagedErrorCode.BATTLE_SCORE_NOT_SATISFY);
		} else {
			return false;
		}
	}

	@Override
	protected void calculate(int num) {
		// nothing todo
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = (resource.getHigh() == 0? Integer.MAX_VALUE : resource.getHigh());
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

}
