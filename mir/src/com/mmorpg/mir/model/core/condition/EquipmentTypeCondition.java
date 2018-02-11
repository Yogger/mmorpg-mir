package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class EquipmentTypeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		EquipmentType type = null;
		
		if (object instanceof EquipmentType) {
			type = (EquipmentType) object;
		}
		
		if (type == null) {
			this.errorObject(object);
		}
		
		return EquipmentType.valueOf(code) == type;
	}

}
