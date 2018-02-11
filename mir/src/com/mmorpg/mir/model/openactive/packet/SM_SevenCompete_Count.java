package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.model.vo.CompeteVO;

public class SM_SevenCompete_Count {

	private int sevenDayCompeteCount;
	
	private Map<Integer, CompeteVO> status;
	
	public static SM_SevenCompete_Count valueOf(Player player) {
		SM_SevenCompete_Count sm = new SM_SevenCompete_Count();
		sm.sevenDayCompeteCount = OpenActiveManager.getInstance().getSevenCompeteRewardCount(player);
		sm.status = OpenActiveManager.getInstance().getCompeteRewardStatus(player);
		return sm;
	}

	public int getSevenDayCompeteCount() {
		return sevenDayCompeteCount;
	}

	public void setSevenDayCompeteCount(int sevenDayCompeteCount) {
		this.sevenDayCompeteCount = sevenDayCompeteCount;
	}

	public Map<Integer, CompeteVO> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, CompeteVO> status) {
		this.status = status;
	}
	
}
