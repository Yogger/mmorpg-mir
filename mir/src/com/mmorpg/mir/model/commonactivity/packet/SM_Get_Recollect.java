package com.mmorpg.mir.model.commonactivity.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.commonactivity.model.RecollectType;
import com.mmorpg.mir.model.commonactivity.packet.vo.RecollectVO;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Get_Recollect {
	private ArrayList<RecollectVO> recollectInfos;

	public static SM_Get_Recollect valueOf(Player player) {
		SM_Get_Recollect sm = new SM_Get_Recollect();
		ArrayList<RecollectVO> vos = new ArrayList<RecollectVO>();
		for (RecollectType type : RecollectType.values()) {
			vos.add(RecollectVO.valueOf(player, type));
		}
		sm.recollectInfos = vos;
		return sm;
	}
	
	public ArrayList<RecollectVO> getRecollectInfos() {
		return recollectInfos;
	}

	public void setRecollectInfos(ArrayList<RecollectVO> recollectInfos) {
		this.recollectInfos = recollectInfos;
	}
	
}
