package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_Country_Appoint {

	private String offical;
	private int index;
	private PlayerSimpleInfo info;

	public static SM_Country_Appoint valueOf(Player player, String offical, int index) {
		SM_Country_Appoint sm = new SM_Country_Appoint();
		sm.info = player.createSimple();
		sm.offical = offical;
		sm.index = index;
		return sm;
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

	public PlayerSimpleInfo getInfo() {
		return info;
	}

	public void setInfo(PlayerSimpleInfo info) {
		this.info = info;
	}

}
