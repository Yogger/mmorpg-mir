package com.mmorpg.mir.model.capturetown.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.capturetown.model.SelfCaptureInfo;

public class SM_Get_My_Fight_Info {

	private int code;
	
	private ArrayList<SelfCaptureInfo> infos;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<SelfCaptureInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<SelfCaptureInfo> infos) {
		this.infos = infos;
	}
	
}
