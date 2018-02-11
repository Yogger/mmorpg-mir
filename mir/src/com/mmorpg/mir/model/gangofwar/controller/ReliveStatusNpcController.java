package com.mmorpg.mir.model.gangofwar.controller;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.world.World;

public class ReliveStatusNpcController extends StatusNpcController {

	/** 观察的守卫 */
	private Boss door;
	private Boss guard;

	synchronized public void playerChangeStatus(Player player) {
		if (getOwner().getSpawn().getStatusDuration() != 0) {
			if (!getOwner().getDurations().containsKey(player.getObjectId())
					|| getOwner().getDurations().get(player.getObjectId()) > System.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.STATUS_NPC_NO_DURATION);
			}
		}
		if (player.getController() instanceof GangOfWarCamp) {
			if (((GangOfWarCamp) player.getController()).getCamps() != Camps.DEFEND) {
				return;
			}
		}

		if (!door.getLifeStats().isAlreadyDead()) {
			return;
		}
		ObjectManager.getInstance().reliveObject(door);
		// ObjectManager.getInstance().reliveObject(guard);
		if (!door.getPosition().isSpawned()) {
			World.getInstance().spawn(door);
			// World.getInstance().spawn(guard);
			getOwner().setStatus(1);
			getOwner().getDurations().clear();

			GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).rank();

			// 通报
			I18nUtils i18nUtils = I18nUtils.valueOf("10203");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("family", I18nPack.valueOf(player.getGang().getName()));
			int doorSeq = 0;
			if (door.getSpawnKey().equals(GangOfWarConfig.getInstance().FIRST_DOOR_SPAW.getValue())) {
				doorSeq = 1;
			} else if (door.getSpawnKey().equals(GangOfWarConfig.getInstance().SECOND_DOOR_SPAW.getValue())) {
				doorSeq = 2;
			} else {
				doorSeq = 3;
			}
			i18nUtils.addParm("x", I18nPack.valueOf(doorSeq + ""));
			ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
					Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
					getOwner().getPosition().getInstanceId());
		}

	}

	public Boss getDoor() {
		return door;
	}

	public void setDoor(Boss door) {
		this.door = door;
	}

	public Boss getGuard() {
		return guard;
	}

	public void setGuard(Boss guard) {
		this.guard = guard;
	}
}
