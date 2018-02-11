package com.mmorpg.mir.model.kingofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerRankInfoVO;

public class SM_KingOfWar_Rank_All {
	private ArrayList<PlayerRankInfoVO> ranks;
	private int totalPage;

	public static SM_KingOfWar_Rank_All valueOf(ArrayList<PlayerRankInfoVO> ranks, int totalPage) {
		SM_KingOfWar_Rank_All sm = new SM_KingOfWar_Rank_All();
		sm.ranks = ranks;
		sm.totalPage = totalPage;
		return sm;
	}

	public ArrayList<PlayerRankInfoVO> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<PlayerRankInfoVO> ranks) {
		this.ranks = ranks;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
