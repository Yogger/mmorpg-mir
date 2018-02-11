package com.mmorpg.mir.model.countrycopy.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;

public class SM_Tech_Copy_Ranks {
	
	private ArrayList<TechCopyRankVO> ranks;

	public ArrayList<TechCopyRankVO> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<TechCopyRankVO> ranks) {
		this.ranks = ranks;
	}

	public static SM_Tech_Copy_Ranks valueOf(ArrayList<TechCopyRankVO> vos) {
		SM_Tech_Copy_Ranks sm = new SM_Tech_Copy_Ranks();
		sm.ranks = vos;
		return sm;
	}
}
