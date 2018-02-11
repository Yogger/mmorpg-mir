package com.mmorpg.mir.model.gang.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_PlayerApply_Gang {
	private String name;

	public static SM_PlayerApply_Gang valueOf(Player player) {
		SM_PlayerApply_Gang sm = new SM_PlayerApply_Gang();
		sm.setName(player.getName());
		return sm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
