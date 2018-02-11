package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.manager.ClawbackManager;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.packet.vo.ClawbackVO;

public class SM_Welfare_Clawback_Open {

	private ArrayList<ClawbackVO> arrayList;

	public static SM_Welfare_Clawback_Open value(Player player) {
		SM_Welfare_Clawback_Open sm = new SM_Welfare_Clawback_Open();
		ArrayList<ClawbackVO> arrayList = New.arrayList();
		for (ClawbackEnum c : ClawbackEnum.values()) {
			ClawbackVO vo = ClawbackVO.valueOf(player, ClawbackManager.getInstance()
					.getClawbackResource(c.getEventId()));
			arrayList.add(vo);
		}
		sm.setArrayList(arrayList);
		return sm;
	}

	public ArrayList<ClawbackVO> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<ClawbackVO> arrayList) {
		this.arrayList = arrayList;
	}

}
