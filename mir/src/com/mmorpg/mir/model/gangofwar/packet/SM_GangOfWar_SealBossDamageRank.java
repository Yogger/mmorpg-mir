package com.mmorpg.mir.model.gangofwar.packet;

import java.util.Map;

import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarBossDamageVO;

public class SM_GangOfWar_SealBossDamageRank {
	private Map<Integer, GangOfWarBossDamageVO> ranks;

	public static SM_GangOfWar_SealBossDamageRank valueOf(Map<Integer, GangOfWarBossDamageVO> ranks) {
		SM_GangOfWar_SealBossDamageRank sm = new SM_GangOfWar_SealBossDamageRank();
		sm.ranks = ranks;
		return sm;
	}

	public Map<Integer, GangOfWarBossDamageVO> getRanks() {
		return ranks;
	}

	public void setRanks(Map<Integer, GangOfWarBossDamageVO> ranks) {
		this.ranks = ranks;
	}
}
