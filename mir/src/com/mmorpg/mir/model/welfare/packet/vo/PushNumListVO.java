package com.mmorpg.mir.model.welfare.packet.vo;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;

public class PushNumListVO {

	private ArrayList<PushNumVO> arrayList;

	public static PushNumListVO valueOf(Player player) {
		PushNumListVO pushListVO = new PushNumListVO();
		ArrayList<PushNumVO> arrayList = New.arrayList();
		for (ClawbackEnum claw : ClawbackEnum.values()) {
			arrayList.add(PushNumVO.valueOf(claw,player));
		}
		pushListVO.setArrayList(arrayList);
		return pushListVO;
	}

	public ArrayList<PushNumVO> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<PushNumVO> arrayList) {
		this.arrayList = arrayList;
	}

}
