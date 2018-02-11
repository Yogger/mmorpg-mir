package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class EquipmentHasSoulCondition extends AbstractCoreCondition {

	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		EquipmentType activatingType = EquipmentType.valueOf(code);
		Equipment activating = player.getEquipmentStorage().getEquip(activatingType);
		
		EquipmentStat extraStat = activating.getExtraStats().get(EquipmentStatType.SOUL_STAT.getValue());
		boolean hasSoul = (extraStat != null && (!extraStat.getContext().contains(EquipmentStat.STAT_NOT_EXIST)));
		
		if (activating != null && hasSoul) {
			return true;
		}
		
		return false;
	}

}
