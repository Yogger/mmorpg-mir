package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;

public class EquipEnhanceNumCondition extends AbstractCoreCondition implements NicknameCondition, AchievementCondition, QuestCondition{

	private int minEnhancedLevel;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) { 
			player = (Player) object;
		}
		
		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		int count = 0;
		
		for (Equipment equip: player.getEquipmentStorage().getEquipments()) {
			if (equip != null && equip.getEnhanceLevel() >= minEnhancedLevel) {
				count++;
			}
		}
		
		return count >= value;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		this.value = resource.getValue();
		this.code = resource.getCode();
		minEnhancedLevel = Integer.parseInt(resource.getCode());
		this.init();
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] {EnhanceEquipmentEvent.class, EquipEquipmentEvent.class};
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] {EnhanceEquipmentEvent.class, EquipEquipmentEvent.class};
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] {EnhanceEquipmentEvent.class, EquipEquipmentEvent.class};
	}

}
