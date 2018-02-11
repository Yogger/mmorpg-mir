package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.model.vo.OldCompeteVO;

public class SM_OldSevenCompete_Count {
	
	private int sevenDayCompeteCount;
	
	private Map<Integer, OldCompeteVO> status;
	
	public static SM_OldSevenCompete_Count valueOf(Player player) {
		SM_OldSevenCompete_Count sm = new SM_OldSevenCompete_Count();
		sm.sevenDayCompeteCount = OpenActiveManager.getInstance().getSevenCompeteRewardCount(player);
		sm.status = OpenActiveManager.getInstance().getOldCompeteRewardStatus(player);
		return sm;
	}

	public int getSevenDayCompeteCount() {
		return sevenDayCompeteCount;
	}

	public void setSevenDayCompeteCount(int sevenDayCompeteCount) {
		this.sevenDayCompeteCount = sevenDayCompeteCount;
	}

	public Map<Integer, OldCompeteVO> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, OldCompeteVO> status) {
		this.status = status;
	}
	
}
