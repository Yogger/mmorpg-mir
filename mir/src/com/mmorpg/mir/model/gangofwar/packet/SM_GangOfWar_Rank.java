package com.mmorpg.mir.model.gangofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarRankItem;

public class SM_GangOfWar_Rank {
	private ArrayList<GangOfWarRankItem> ranks;
	private int totalPage;

	public static SM_GangOfWar_Rank valueOf(ArrayList<GangOfWarRankItem> ranks, int totalPage) {
		SM_GangOfWar_Rank sm = new SM_GangOfWar_Rank();
		sm.ranks = ranks;
		sm.totalPage = totalPage;
		return sm;
	}

	public ArrayList<GangOfWarRankItem> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<GangOfWarRankItem> ranks) {
		this.ranks = ranks;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
