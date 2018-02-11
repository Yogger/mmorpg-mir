package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class EquipSameLevelCondition extends AbstractCoreCondition {

	private String firstEquip;
	private String secondEquip;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		EquipmentType activatingType = EquipmentType.valueOf(firstEquip);
		Equipment activating = player.getEquipmentStorage().getEquip(activatingType);
		EquipmentType factorType = EquipmentType.valueOf(secondEquip);
		Equipment factor = player.getEquipmentStorage().getEquip(factorType);
		
		boolean notNull = activating != null && factor != null;
		boolean sameLevel = activating.getResource().getLevel() == factor.getResource().getLevel();
		
		if (notNull && sameLevel) 
			return true;
		
		return false;
	}

	public String getFirstEquip() {
		return firstEquip;
	}

	public void setFirstEquip(String firstEquip) {
		this.firstEquip = firstEquip;
	}

	public String getSecondEquip() {
		return secondEquip;
	}

	public void setSecondEquip(String secondEquip) {
		this.secondEquip = secondEquip;
	}

}
