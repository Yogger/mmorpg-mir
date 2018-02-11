package com.mmorpg.mir.model.mergeactive.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mergeactive.manager.MergeActiveManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;

public class SM_Get_Merge_Consume {

	private ArrayList<String> canRecieves;
	
	private int consumeGold;
	
	private int rank;
	
	public static SM_Get_Merge_Consume valueOf(Player player) {
		SM_Get_Merge_Consume sm = new SM_Get_Merge_Consume();
		sm.consumeGold = player.getMergeActive().getConsumeCompete().getConsumeGold();
		sm.canRecieves = MergeActiveManager.getInstance().getConsumeCanRecievesReward(player);
		sm.rank = WorldRankManager.getInstance().getMyRank(player, RankType.MERGE_ACTIVITY_CONSUME);
		return sm;
	}

	public final ArrayList<String> getCanRecieves() {
		return canRecieves;
	}

	public final void setCanRecieves(ArrayList<String> canRecieves) {
		this.canRecieves = canRecieves;
	}

	public final int getConsumeGold() {
		return consumeGold;
	}

	public final void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}

	public final int getRank() {
		return rank;
	}

	public final void setRank(int rank) {
		this.rank = rank;
	}

}
