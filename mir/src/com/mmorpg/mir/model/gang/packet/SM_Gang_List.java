package com.mmorpg.mir.model.gang.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gang.model.GangSimpleVO;

public class SM_Gang_List {
	private ArrayList<GangSimpleVO> vos = new ArrayList<GangSimpleVO>();

	public ArrayList<GangSimpleVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<GangSimpleVO> vos) {
		this.vos = vos;
	}
}