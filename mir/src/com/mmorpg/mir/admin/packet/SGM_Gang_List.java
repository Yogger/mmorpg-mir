package com.mmorpg.mir.admin.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gang.model.GangSimpleVO;

public class SGM_Gang_List {
	private ArrayList<GangSimpleVO> vos = new ArrayList<GangSimpleVO>();

	public ArrayList<GangSimpleVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<GangSimpleVO> vos) {
		this.vos = vos;
	}
}