package com.mmorpg.mir.model.chat.model.show;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.model.ShowType;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;

@Component
public class ItemShowHandle extends ShowHandle {

	private static String GUID = "GUID";

	@Override
	public ShowType getType() {
		return ShowType.ITEM;
	}

	@Override
	public Object createShowObject(Player player, HashMap<String, String> requestShow) {
		if (requestShow.get(GUID) == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_ITEM);
		}
		long guid = Long.valueOf(requestShow.get(GUID));
		AbstractItem item = player.getPack().getItemByGuid(guid);
		if (item == null) {
			item = player.getEquipmentStorage().getByGuid(guid);
		}
		if (item == null) {
			item = player.getHorseEquipmentStorage().getByGuid(guid);
		}

		if (item == null) {
			item = player.getLifeGridPool().getEquipStorage().getItemByGuid(guid);
		}

		if (item == null) {
			item = player.getLifeGridPool().getPackStorage().getItemByGuid(guid);
		}

		if (item == null) {
			item = player.getLifeGridPool().getHouseStorage().getItemByGuid(guid);
		}

		if (item == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_ITEM);
		}
		ItemShow itemShow = new ItemShow();
		itemShow.setKey(item.getKey());
		itemShow.setOwner(player.getName());
		itemShow.setItem(item);
		return itemShow;
	}
}
