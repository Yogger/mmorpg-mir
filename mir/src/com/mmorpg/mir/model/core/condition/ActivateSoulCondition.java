package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class ActivateSoulCondition extends AbstractCoreCondition {

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
		EquipmentType factorType = ItemManager.getInstance().getActivateMap().get(activatingType);
		Equipment factor = player.getEquipmentStorage().getEquip(factorType);
		
		boolean notNull = activating != null && factor != null;
		boolean hasSoul = activating.getExtraStats().get(EquipmentStatType.SOUL_STAT.getValue()) != null;
		boolean sameElement = activating.hasElement() && activating.getElement() == factor.getElement();
		boolean sameLevel = activating.getResource().getLevel() == factor.getResource().getLevel();
		boolean sameRole = factor.getResource().getRoletype() == activating.getResource().getRoletype();
		
		if (notNull && hasSoul && sameElement && sameLevel && sameRole) {
			return true;
		}
		
		return false;
	}

}
