package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.vo.OldCompeteVO;

public class SM_OldSevenCompete_Push {
	private OldCompeteVO vo;

	private int type;

	private int sevenDayCompeteCount;

	public static SM_OldSevenCompete_Push valueOf(Player player, int rankTypeValue, OldCompeteVO competeVO) {
		SM_OldSevenCompete_Push sm = new SM_OldSevenCompete_Push();
		sm.vo = competeVO;
		sm.type = rankTypeValue;
		sm.sevenDayCompeteCount = player.getOpenActive().getOldCanRecieved().size();
		return sm;
	}

	public OldCompeteVO getVo() {
		return vo;
	}

	public void setVo(OldCompeteVO vo) {
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
