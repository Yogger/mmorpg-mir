package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.model.Skill;

public class LifeCondition extends AbstractCoreCondition {

	private int low;

	private int high;

	@Override
	public boolean verify(Object object) {
		Creature creature = null;

		if (object instanceof Creature) {
			creature = (Creature) object;
		}
		
		if (object instanceof Skill) {
			Skill skill = (Skill) object;
			creature = skill.getEffector();
		}

		if (creature == null) {
			this.errorObject(object);
		}

		int percent = (int) (((double) creature.getLifeStats().getCurrentHp() / (double) creature.getLifeStats()
				.getMaxHp()) * 10000);

		return percent >= low && percent <= high;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public void setHigh(int high) {
		this.high = high;
	}

}
