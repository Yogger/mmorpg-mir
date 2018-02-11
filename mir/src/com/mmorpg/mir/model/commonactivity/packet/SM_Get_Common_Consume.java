package com.mmorpg.mir.model.commonactivity.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
import com.mmorpg.mir.model.commonactivity.resource.CommonConsumeActiveResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;

public class SM_Get_Common_Consume {
	private ArrayList<String> canRecieves;

	private int consumeGold;
	
	private String activityName;

	private int rank;

	public static SM_Get_Common_Consume valueOf(Player player, String activityName) {
		SM_Get_Common_Consume sm = new SM_Get_Common_Consume();
		sm.consumeGold = player.getCommonActivityPool().getConsumeActives().get(activityName).getConsumeGold();
		sm.canRecieves = CommonActivityManager.getInstance().getConsumeCanRecievesReward(player, activityName);
		CommonConsumeActiveResource resource = CommonActivityConfig.getInstance().consumeStorages.getIndex(
				CommonConsumeActiveResource.ACTVITY_NAME, activityName).get(0);
		sm.rank = WorldRankManager.getInstance().getMyRank(player, RankType.valueOf(resource.getRankType()));
		sm.setActivityName(activityName);
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

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
