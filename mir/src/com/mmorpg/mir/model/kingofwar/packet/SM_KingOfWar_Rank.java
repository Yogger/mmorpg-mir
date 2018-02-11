package com.mmorpg.mir.model.kingofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerRankInfoVO;

public class SM_KingOfWar_Rank {
	private ArrayList<PlayerRankInfoVO> ranks;

	public static SM_KingOfWar_Rank valueOf(ArrayList<PlayerRankInfoVO> ranks) {
		SM_KingOfWar_Rank sm = new SM_KingOfWar_Rank();
		sm.ranks = ranks;
		return sm;
	}

	public ArrayList<PlayerRankInfoVO> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<PlayerRankInfoVO> ranks) {
		this.ranks = ranks;
	}

}
