package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.quest.model.Quest;

public class EquipQualityNumsCondition extends AbstractCoreCondition implements QuestCondition{

	private int low;
	
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
			if (equip != null && equip.getResource().getQuality() >= low) {
				count++;
			}
		}
		
		if (count >= value) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		low = resource.getLow();
	}

	@Override
    public Class<?>[] getEvent() {
		return new Class<?>[] {EquipEquipmentEvent.class};
    }

}
