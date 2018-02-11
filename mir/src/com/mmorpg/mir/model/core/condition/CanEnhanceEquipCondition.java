package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;

public class CanEnhanceEquipCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		boolean hasEnhanceEquip = false;
		ItemResource resource = ItemManager.getInstance().getResource(code);
		int maxLevel = ItemConfig.getInstance().getEquipmentMaxEnhanceLevel();
		for (Equipment equipment : player.getEquipmentStorage().getEquipments()) {		
			if (equipment == null) {
				continue;
			}
			if (equipment.getEnhanceLevel() >= maxLevel || equipment.getEnhanceLevel() > resource.getEnhanceHigh()) {
				continue;
			}
			hasEnhanceEquip = true;
		}
		if (!hasEnhanceEquip) {
			throw new ManagedException(ManagedErrorCode.DONNOT_HAVE_ENHANCEABLE_EQUIP);
		}
		return true;
	}

}
