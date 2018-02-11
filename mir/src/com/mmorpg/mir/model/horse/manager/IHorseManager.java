package com.mmorpg.mir.model.horse.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.resource.HorseAttachResource;
import com.mmorpg.mir.model.horse.resource.HorseResource;

public interface IHorseManager {

	Integer getClearNowBlessTimeInterval();

	HorseAttachResource getHorseAttachResource(int id);

	HorseResource getHorseResource(int id);

	long getIntervalTime();

	boolean isOpen(Player player);
}
