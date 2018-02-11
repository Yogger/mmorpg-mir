package com.mmorpg.mir.model.openactive.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;

public class OldSevenCompeteVO {
	private ArrayList<String> recievedIds;

	private int sevenDayCompeteCount;

	public static OldSevenCompeteVO valueOf(Player player) {
		OldSevenCompeteVO vo = new OldSevenCompeteVO();
		vo.sevenDayCompeteCount = OpenActiveManager.getInstance().getOldSevenCompeteRewardCount(player);
		ArrayList<String> recieved = new ArrayList<String>();
		recieved.addAll(player.getOpenActive().getCompeteRankActivity(
				CompeteRankValue.OLD_SOUL_RANK.getRankTypeValue()).getRewarded());
		vo.recievedIds = recieved;
		return vo;
	}

	public ArrayList<String> getRecievedIds() {
		return recievedIds;
	}

	public void setRecievedIds(ArrayList<String> recievedIds) {
		this.recievedIds = recievedIds;
	}

	public int getSevenDayCompeteCount() {
		return sevenDayCompeteCount;
	}

	public void setSevenDayCompeteCount(int sevenDayCompeteCount) {
		this.sevenDayCompeteCount = sevenDayCompeteCount;
	}

}
