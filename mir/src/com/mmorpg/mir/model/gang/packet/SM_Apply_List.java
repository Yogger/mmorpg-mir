package com.mmorpg.mir.model.gang.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gang.model.Apply;
import com.mmorpg.mir.model.gang.model.ApplyVO;
import com.mmorpg.mir.model.gang.model.Gang;

public class SM_Apply_List {
	private ArrayList<ApplyVO> vos = new ArrayList<ApplyVO>();

	public static SM_Apply_List valueOf(Gang gang) {
		SM_Apply_List sm = new SM_Apply_List();
		for (Apply a : gang.getApplies().values()) {
			sm.getVos().add(a.createVO());
		}
		return sm;
	}

	public ArrayList<ApplyVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<ApplyVO> vos) {
		this.vos = vos;
	}

}