package com.mmorpg.mir.model.group.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.group.model.GroupSimpleVO;

public class SM_GROUP_LIST {
	private ArrayList<GroupSimpleVO> vos = new ArrayList<GroupSimpleVO>();

	public ArrayList<GroupSimpleVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<GroupSimpleVO> vos) {
		this.vos = vos;
	}

}
