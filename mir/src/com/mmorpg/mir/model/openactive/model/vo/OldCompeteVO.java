package com.mmorpg.mir.model.openactive.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;

public class OldCompeteVO {

	private ArrayList<String> canRecieves;
	
	private int rank;
	
	private int value;
	
	public static OldCompeteVO valueOf(CompeteRankValue rankValue, Player player) {
		OldCompeteVO vo = new OldCompeteVO();
		vo.value = player.getOpenActive().getCompeteRankActivity(rankValue.getRankTypeValue()).getCompeteValue();
		vo.rank = WorldRankManager.getInstance().getMyRank(player, RankType.valueOf(rankValue.getRankTypeValue()));
		vo.canRecieves = rankValue.getOldCanRewardCount(player);
		return vo;
	}

	public ArrayList<String> getCanRecieves() {
		return canRecieves;
	}

	public void setCanRecieves(ArrayList<String> canRecieves) {
		this.canRecieves = canRecieves;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
