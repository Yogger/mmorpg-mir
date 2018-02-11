package com.mmorpg.mir.model.countrycopy.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Get_Encourage_List {
	private ArrayList<String> playerNames;
	
	public static SM_Get_Encourage_List valueOf(CountryCopy copy) {
		SM_Get_Encourage_List sm = new SM_Get_Encourage_List();
		ArrayList<String> names = new ArrayList<String>();
		int size = copy.getEncourageList().size();
		for (int i = 1; i <= size; i++) {
			Player player = copy.getEncourageList().get(i);
			if (player != null) {
				names.add(player.getName());
			}
		}
		sm.playerNames = names;
		return sm;
	}

	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(ArrayList<String> playerNames) {
		this.playerNames = playerNames;
	}
	
}
