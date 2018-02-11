package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class BelongToDefenseEquipCondition extends AbstractCoreCondition{
	
	@Override
	public boolean verify(Object object) {
		EquipmentType type = null;
		
		if (object instanceof EquipmentType) {
			type = (EquipmentType) object;
		}
		
		if (type == null)
			this.errorObject(object);
		
		for (Integer equipOrdinal: ItemManager.getInstance().getDefenseEquipGroup()) {
			if (equipOrdinal.equals(type.ordinal()))
				return true;
		}
		return false;
	}

}
