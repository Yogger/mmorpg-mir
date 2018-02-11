package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.vo.CompeteVO;

public class SM_SevenCompete_Push {

	private CompeteVO vo;
	
	private int type;

	private int sevenDayCompeteCount;
	
	public static SM_SevenCompete_Push valueOf(Player player, int rankTypeValue, CompeteVO competeVO) {
		SM_SevenCompete_Push sm = new SM_SevenCompete_Push();
		sm.vo = competeVO;
		sm.type = rankTypeValue;
		sm.sevenDayCompeteCount = player.getOpenActive().getCanRecieved().size();
		return sm;
	}

	public CompeteVO getVo() {
		return vo;
	}

	public void setVo(CompeteVO vo) {
		this.vo = vo;
	}

	public int getSevenDayCompeteCount() {
		return sevenDayCompeteCount;
	}

	public void setSevenDayCompeteCount(int sevenDayCompeteCount) {
		this.sevenDayCompeteCount = sevenDayCompeteCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
