package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.NpcController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.packet.SM_PlayerInfo;
import com.mmorpg.mir.model.world.WorldPosition;

public class TownPlayerNpc extends Monster {

	public TownPlayerNpc(long objId, NpcController controller, WorldPosition position) {
		super(objId, controller, position);
	}

	private SM_PlayerInfo playerInfo;

	protected boolean canSeeServant(VisibleObject visibleObject) {
		return false;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.TOWN_PLAYER_NPC;
	}

	public SM_PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(SM_PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}

}
