package com.mmorpg.mir.model.core.condition.boss;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;

/**
 * BOSS伤害排名
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-3
 * 
 */
public class BossRankCondition extends AbstractCoreCondition {

	private int low;
	private int high;

	@Override
	public boolean verify(Object object) {
		int rank = (Integer) object;
		if (low <= rank && rank <= high) {
			return true;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
