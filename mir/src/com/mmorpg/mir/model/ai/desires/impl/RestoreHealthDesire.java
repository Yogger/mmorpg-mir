/*  
 *  This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.windforce.common.utility.DateUtils;

/**
 * @author ATracer
 * 
 */
public class RestoreHealthDesire extends AbstractDesire {
	private Npc owner;
	private long restoreHpValue;
	// 最后回血的时间
	private long lastRestoreHealth;

	public RestoreHealthDesire(Npc owner, int desirePower) {
		super(desirePower);
		this.owner = owner;
		if (owner instanceof Npc) {
			restoreHpValue = (long) Math.ceil((owner.getLifeStats().getMaxHp() * (((Npc) owner).getObjectResource()
					.getRestoreHp() * 1.0 / 10000)));
		} else {
			restoreHpValue = owner.getLifeStats().getMaxHp() / 5;
		}
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;

		long unit = 0;
		long now = System.currentTimeMillis();
		if (lastRestoreHealth == 0) {
			// 第一次恢复一个单位
			unit = 1;
			lastRestoreHealth = now;
		} else {
			long interval = now - lastRestoreHealth;
			if (interval >= DateUtils.MILLIS_PER_SECOND) {
				unit = (long) (interval / DateUtils.MILLIS_PER_SECOND);
				lastRestoreHealth = lastRestoreHealth + (unit * DateUtils.MILLIS_PER_SECOND);
			}
		}

		// 这里开始回血
		owner.getLifeStats().increaseHp(restoreHpValue * unit);
		if (owner.getLifeStats().isFullyRestoredHp()) {
			ai.handleEvent(Event.RESTORED_HEALTH);
			owner.getController().onFightOff();
			return false;
		}

		return true;
	}

	@Override
	public int getExecutionInterval() {
		return 1;
	}

	@Override
	public void onClear() {

	}
}
