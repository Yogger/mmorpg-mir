package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.PetItem;

public class SM_Call_Pet {
	private int code;
	
	private PetItem petItem;

	public static SM_Call_Pet valueOf(Player player) {
		SM_Call_Pet sm = new SM_Call_Pet();
		sm.petItem = player.getEquipmentStorage().getPetItem();
		return sm;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public PetItem getPetItem() {
		return petItem;
	}

	public void setPetItem(PetItem petItem) {
		this.petItem = petItem;
	}

}
