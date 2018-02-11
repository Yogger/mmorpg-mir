package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class SameElementCondition extends AbstractCoreCondition {

	private String firstEquipment;
	private String secondEquipment;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		EquipmentType activatingType = EquipmentType.valueOf(firstEquipment);
		Equipment activating = player.getEquipmentStorage().getEquip(activatingType);
		EquipmentType factorType = EquipmentType.valueOf(secondEquipment);
		Equipment factor = player.getEquipmentStorage().getEquip(factorType);
		
		
		boolean notNull = activating != null && factor != null;
		boolean sameElement = activating.hasElement() && activating.getElement() == factor.getElement();
		
		if (notNull && sameElement)
			return true;
		
		return false;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		firstEquipment = resource.getStartDate();
		secondEquipment = resource.getEndDate();
	}

	public String getFirstEquipment() {
		return firstEquipment;
	}

	public void setFirstEquipment(String firstEquipment) {
		this.firstEquipment = firstEquipment;
	}

	public String getSecondEquipment() {
		return secondEquipment;
	}

	public void setSecondEquipment(String secondEquipment) {
		this.secondEquipment = secondEquipment;
	}

}
