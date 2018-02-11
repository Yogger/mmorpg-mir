package com.mmorpg.mir.model.openactive.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;

public class SevenCompeteVO {
	private ArrayList<String> recievedIds;
	
	private int sevenDayCompeteCount;
	
	public static SevenCompeteVO valueOf(Player player) {
		SevenCompeteVO vo = new SevenCompeteVO();
		vo.sevenDayCompeteCount = OpenActiveManager.getInstance().getSevenCompeteRewardCount(player);
		ArrayList<String> recieved = new ArrayList<String>();
		for (CompeteRankValue type: CompeteRankValue.values()) {
			if (type == CompeteRankValue.OLD_SOUL_RANK) {
				continue;
			}
			recieved.addAll(player.getOpenActive().getCompeteRankActivity(type.getRankTypeValue()).getRewarded());
		}
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
