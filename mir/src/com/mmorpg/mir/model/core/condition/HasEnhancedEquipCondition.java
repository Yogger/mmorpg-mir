package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;

public class HasEnhancedEquipCondition extends AbstractCoreCondition implements QuestCondition{

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
		
		for (Equipment equip: player.getEquipmentStorage().getEquipments()) {
			if (equip != null && equip.getEnhanceLevel() >= value) {
				return true;
			}
		}
		
		for (AbstractItem item: player.getPack().getItems()) {
			if (item != null && item instanceof Equipment) {
				Equipment equip = (Equipment) item;
				if (equip.getEnhanceLevel() >= value) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
    public Class<?>[] getEvent() {
	    return new Class<?>[] {EnhanceEquipmentEvent.class};
    }

}
