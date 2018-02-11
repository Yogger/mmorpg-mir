package com.mmorpg.mir.model.countrycopy.worldmap;

import java.util.Iterator;
import java.util.List;

import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.gameobjects.JourObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class CountryCopyWorldMapInstance extends WorldMapInstance {

	public CountryCopyWorldMapInstance(WorldMap parent, int instanceId, CountryCopy countryCopy) {
		super(parent, instanceId);
		this.countryCopy = countryCopy;
	}
	
	private CountryCopy countryCopy;

	@Override
	public void addObject(VisibleObject object) {
		super.addObject(object);
		if (countryCopy.isWarring()) {
			if (object instanceof Player) {
				Player player = (Player) object;
				if (countryCopy.getEncourageList().size() > 0) {
					Skill skill = SkillEngine.getInstance().getSkill(
							null,
							CountryCopyConfig.getInstance().ENCOURAGE_SKILLIDS.getValue()[countryCopy
									.getEncourageList().size() - 1], player.getObjectId(), 0, 0, player, null);
					List<Effect> effects = skill.noEffectorUseSkill();
					player.getCountryCopyInfo().setEffects(effects);
				}
			}
		}
	}

	@Override
	public synchronized void removeObject(JourObject object) {
		super.removeObject(object);
		if (object instanceof Player) {
			Player player = (Player) object;
			player.getCountryCopyInfo().endEffect();
		}
	}

	public synchronized void upgradePlayerEffect() {
		if (countryCopy.isWarring()) {
			Iterator<Player> iter = playerIterator();
			while (iter.hasNext()) {
				Player player = (Player) iter.next();
				if (countryCopy.getEncourageList().size() > 0) {
					Skill skill = SkillEngine.getInstance().getSkill(
							null,
							CountryCopyConfig.getInstance().ENCOURAGE_SKILLIDS.getValue()[countryCopy
									.getEncourageList().size() - 1], player.getObjectId(), 0, 0, player, null);
					List<Effect> effects = skill.noEffectorUseSkill();
					player.getCountryCopyInfo().setEffects(effects);
				}
			}
		}
	}
	
}
