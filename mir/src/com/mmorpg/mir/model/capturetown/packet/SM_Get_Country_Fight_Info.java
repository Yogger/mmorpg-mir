package com.mmorpg.mir.model.capturetown.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.capturetown.model.CountryCaptureInfo;

public class SM_Get_Country_Fight_Info {

	private int code;
	
	private ArrayList<CountryCaptureInfo> infos;

	public static SM_Get_Country_Fight_Info valueOf(ArrayList<CountryCaptureInfo> infos) {
		SM_Get_Country_Fight_Info sm = new SM_Get_Country_Fight_Info();
		sm.infos = infos;
		return sm;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<CountryCaptureInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<CountryCaptureInfo> infos) {
		this.infos = infos;
	}
	
}
