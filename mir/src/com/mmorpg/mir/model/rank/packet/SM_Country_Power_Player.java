package com.mmorpg.mir.model.rank.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.rank.model.RankRow;

public class SM_Country_Power_Player {
	
	private ArrayList<RankRow> powerPlayers;

	public static SM_Country_Power_Player valueOf(ArrayList<RankRow> rank) {
		SM_Country_Power_Player sm = new SM_Country_Power_Player();
		sm.powerPlayers = rank;
		return sm;
	}
	
	public ArrayList<RankRow> getPowerPlayers() {
		return powerPlayers;
	}

	public void setPowerPlayers(ArrayList<RankRow> powerPlayers) {
		this.powerPlayers = powerPlayers;
	}
	
}
