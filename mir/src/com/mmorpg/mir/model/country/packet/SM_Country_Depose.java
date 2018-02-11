package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Country_Depose {

	private String name;
	private String offical;
	private int index;

	public static SM_Country_Depose valueOf(Player player, String offical, int index) {
		SM_Country_Depose sm = new SM_Country_Depose();
		sm.name = player.getName();
		sm.offical = offical;
		sm.index = index;
		return sm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOffical() {
		return offical;
	}

	public void setOffical(String offical) {
		this.offical = offical;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
