package com.mmorpg.mir.model.welfare.manager;

import java.util.ArrayList;
import java.util.Collection;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.resource.GiftResource;

public interface IWelfareConfigValueManager {
	GiftResource getOneOffGiftResource(int id);

	Collection<GiftResource> getAllOneOffGiftResources();
	
	ArrayList<Boolean> oneOffGiftStatus(Player player);
}
