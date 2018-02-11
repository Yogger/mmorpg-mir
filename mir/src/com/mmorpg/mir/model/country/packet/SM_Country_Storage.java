package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.CountryStorge;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.storage.ItemStorage;

public class SM_Country_Storage {
	private ItemStorage itemStorage;

	public static SM_Country_Storage valueOf(Player player, CountryStorge countryStorge) {
		SM_Country_Storage sm = new SM_Country_Storage();
		sm.itemStorage = countryStorge.getStorge();
		return sm;
	}

	public ItemStorage getItemStorage() {
		return itemStorage;
	}

	public void setItemStorage(ItemStorage itemStorage) {
		this.itemStorage = itemStorage;
	}

}
