package com.mmorpg.mir.model.achievement.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.achievement.model.AchievementItem;
import com.mmorpg.mir.model.achievement.resource.AchievementResource;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

public class AchievementManager implements ResourceReload {

	private Map<Class<?>, List<Integer>> achievementEventConditions;

	@Static
	private Storage<Integer, AchievementResource> resources;

	@Override
	public void reload() {
		Map<Class<?>, List<Integer>> achievementEventsTemp = New.hashMap();
		for (AchievementResource resource : resources.getAll()) {
			for (AbstractCoreCondition condition : resource.getConditions().getConditionList()) {
				if (condition instanceof AchievementCondition) {
					AchievementCondition ac = (AchievementCondition) condition;
					for (Class<?> clazz : ac.getAchievementEvent()) {
						if (!achievementEventsTemp.containsKey(clazz)) {
							achievementEventsTemp.put(clazz, new ArrayList<Integer>());
						}
						achievementEventsTemp.get(clazz).add(resource.getId());
					}
				}
			}
		}

		this.achievementEventConditions = achievementEventsTemp;
	}

	/**
	 * 尝试完成成就
	 * 
	 * @param id
	 * @param player
	 */
	public void tryComplete(int id, Player player) {
		AchievementResource achievementResource = resources.get(id, true);
		AchievementItem achievementItem = player.getAchievement().getOrCreate(id);
		if (achievementResource.getConditions().verify(player, false)) {
			achievementItem.setCompleted(true);
			// TODO sendPacket
		}

	}

	/**
	 * 获取该事件关联的所有成就
	 * 
	 * @param event
	 * @return
	 */
	public List<Integer> getAchievementIds(IEvent event) {
		if (achievementEventConditions.containsKey(event)) {
			return achievementEventConditions.get(event);
		}
		return new ArrayList<Integer>();
	}

	@Override
	public Class<?> getResourceClass() {
		return AchievementResource.class;
	}

}
