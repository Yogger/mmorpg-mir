/*
 * This file is part of aion-unique <aion-unique.org>.
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
package com.mmorpg.mir.model.controllers.effect;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.EffectControllerDB;
import com.mmorpg.mir.model.skill.model.EffectDB;
import com.mmorpg.mir.model.skill.model.SkillTemplate;
import com.windforce.common.utility.JsonUtils;

/**
 * @author ATracer
 * 
 */
public class PlayerEffectController extends EffectController {

	private Map<String, Long> buffAccumulateMap = New.hashMap();

	public PlayerEffectController(Creature owner) {
		super(owner);
	}

	public void save() {
		Player player = (Player) getOwner();
		String json = JsonUtils.object2String(createEffectControllerDB());
		player.getPlayerEnt().setEffectControllerDBJson(json);
	}

	public void unSave() {
		Player player = (Player) getOwner();
		EffectControllerDB controllerDB = JsonUtils.string2Object(player.getPlayerEnt().getEffectControllerDBJson(),
				EffectControllerDB.class);
		for (EffectDB db : controllerDB.getEffectDBs()) {
			addSavedEffect(db);
		}
		buffAccumulateMap = controllerDB.getBuffAccumulate();
	}

	public EffectControllerDB createEffectControllerDB() {
		EffectControllerDB edb = new EffectControllerDB();
		for (Effect effect : getAbnormalEffects()) {
			edb.addEffectDB(effect.creatToDB());
		}
		edb.setBuffAccumulate(buffAccumulateMap);
		return edb;
	}

	public void addSavedEffect(EffectDB db) {
		SkillTemplate template = SkillEngine.getInstance().loadOrCreateSkillTemplate(db.getSkillId());

		long remainingTime = 0;
		if (template.isUnlineDuration()) {
			remainingTime = db.getEndTime() - System.currentTimeMillis();
		} else {
			remainingTime = db.getEndTime() - db.getSaveTime();
		}

		if (remainingTime <= 0)
			return;

		Effect effect = new Effect(getOwner(), getOwner(), template, db.getSkillLevel(), remainingTime);
		effect.setReserved3(db.getReserved3());
		abnormalEffectMap.put(effect.getGroup(), effect);
		effect.addAllEffectToSucess();
		effect.startEffect(true);

		// PacketSendUtility.sendPacket(getOwner(), new
		// SM_ABNORMAL_EFFECT(getOwner().getObjectId(), abnormals,
		// Collections.singletonList(effect)));
		// broadCastEffects();

	}

	public void logoutRemoveAllEffects() {
		removeAllEffects();
	}

	@JsonIgnore
	public long getAndDelete(String s) {
		if (buffAccumulateMap == null) {
			buffAccumulateMap = New.hashMap();
			return 0l;
		}
		Long total = buffAccumulateMap.remove(s);
		return total == null ? 0L : total.longValue();
	}

	@JsonIgnore
	public void accumulateBuff(String s, long val) {
		if (buffAccumulateMap.containsKey(s)) {
			buffAccumulateMap.put(s, buffAccumulateMap.get(s) + val);
		} else {
			buffAccumulateMap.put(s, val);
		}
	}

	public Map<String, Long> getBuffAccumulateMap() {
		return buffAccumulateMap;
	}

	public void setBuffAccumulateMap(Map<String, Long> buffAccumulateMap) {
		this.buffAccumulateMap = buffAccumulateMap;
	}
}
