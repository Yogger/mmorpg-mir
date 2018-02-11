package com.mmorpg.mir.model.gangofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarRankItem;

public class SM_GangOfWar_RankSimple {
	private ArrayList<GangOfWarRankItem> ranks;

	public static SM_GangOfWar_RankSimple valueOf(ArrayList<GangOfWarRankItem> ranks) {
		SM_GangOfWar_RankSimple sm = new SM_GangOfWar_RankSimple();
		sm.ranks = ranks;
		return sm;
	}

	public ArrayList<GangOfWarRankItem> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<GangOfWarRankItem> ranks) {
		this.ranks = ranks;
	}

}
